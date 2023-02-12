package com.example.todolist.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.request.RequestOptions
import com.example.todolist.R
import com.example.todolist.model.Priority
import com.example.todolist.model.Response
import com.example.todolist.model.Task
import com.example.todolist.network.FirebaseCallback
import com.example.todolist.ui.theme.TodoListTheme
import com.example.todolist.viewmodel.TaskUiState
import com.example.todolist.viewmodel.TaskViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@Suppress("OPT_IN_IS_NOT_ENABLED")
@AndroidEntryPoint
class TodoListActivity : ComponentActivity() {

    private lateinit var viewModel: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mAuth = FirebaseAuth.getInstance()
        Log.d("AnhHuy", "onCreate: ${mAuth.currentUser?.displayName}")

        viewModel = ViewModelProvider(this)[TaskViewModel::class.java]

        setContent {
            TodoListTheme {
                HomeScreen(
                    taskUiState = viewModel.taskUiState,
                    retryAction = { viewModel.getTasks() }
                )
            }
        }
    }

    @Composable
    private fun HomeScreen(
        taskUiState: TaskUiState,
        retryAction: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        when (taskUiState) {
            is TaskUiState.Loading -> {
                LoadingScreen(modifier)
                Log.d("AnhHuy", "Loading State")
            }
            is TaskUiState.Success -> {
                TodoListScreen(taskUiState.tasks, modifier)
                Log.d("AnhHuy", "Success State")
            }
            is TaskUiState.Error -> {
                ErrorScreen(taskUiState.error, retryAction, modifier)
                Log.d("AnhHuy", "Error State")
            }
        }
    }

    @Composable
    private fun LoadingScreen(modifier: Modifier = Modifier) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colors.primary,
                strokeWidth = 4.dp,
                modifier = Modifier.size(50.dp)
            )
        }
    }

    @Composable
    private fun ErrorScreen(
        error: String,
        retryAction: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Error: $error")
            Button(onClick = retryAction) {
                Text("Retry")
            }
        }
    }

    @Composable
    private fun TodoListScreen(
        taskList: List<Task>,
        modifier: Modifier = Modifier
    ) {

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = { AppTopBar() }
        ) {
            Box() {
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxSize()
                ) {
                    Spacer(modifier = Modifier.height(10.dp))

                    TaskList(taskList = taskList)

                    Spacer(Modifier.weight(1f))
                }

                FloatingActionButton(
                    onClick = {
                        startActivity(Intent(this@TodoListActivity, AddTaskActivity::class.java))
                    },
                    backgroundColor = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .padding(20.dp)
                        .align(Alignment.BottomEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add task",
                        modifier = Modifier
                            .size(20.dp)
                    )
                }
            }
        }
    }

    @Composable
    private fun TaskList(taskList: List<Task>) {
        // get task list from Firebase

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(taskList.size) { index ->
                TaskItem(taskList[index])
            }
        }
    }

    @Composable
    private fun TaskItem(task: Task) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            elevation = 8.dp,
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = task.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = task.description,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                CircleShape(colorRes = when (task.priority) {
                    Priority.HIGH -> R.color.red
                    Priority.MEDIUM -> R.color.green
                    Priority.LOW -> R.color.black
                })
            }
        }
    }

    @Preview
    @Composable
    private fun AppTopBar(modifier: Modifier = Modifier) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colors.surface),
        ) {
            Spacer(Modifier.width(15.dp))

            Image(
                painter = painterResource(id = R.mipmap.todo),
                contentDescription = "App icon",
                modifier = Modifier
                    .size(30.dp)
                    .align(Alignment.CenterVertically),
            )

            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.h2,
                fontSize = 30.sp,
                modifier = Modifier
                    .padding(start = 10.dp)
                    .align(Alignment.CenterVertically),
            )


            Spacer(Modifier.weight(1f))

            IconButton(onClick = { /*search task by task's name*/ }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search task by task's name",
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.CenterVertically)
                )
            }

            IconButton(onClick = { /*task filter*/}) {
                Icon(
                    painter = painterResource(id = R.drawable.filter),
                    contentDescription = "Task filter",
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.CenterVertically)
                )
            }
        }
    }

    @Preview(showBackground = true)
    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    private fun TodoListTopBar() {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Glide.with(applicationContext)
                .asBitmap()
                .load("https://sohanews.sohacdn.com/thumb_w/660/160588918557773824/2021/9/14/photo1631588006082-16315880063578503538.jpg")
                .apply(RequestOptions.circleCropTransform())
        }
    }
}
@Composable
fun CircleShape(colorRes: Int){
    Column(modifier = Modifier
        .wrapContentSize(Alignment.Center)) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(15.dp)
                .clip(shape = androidx.compose.foundation.shape.CircleShape)
                .background(
                    color = colorResource(id = colorRes)
                )
        )
    }
}

