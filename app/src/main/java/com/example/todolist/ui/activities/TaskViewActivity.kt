package com.example.todolist.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todolist.R
import com.example.todolist.model.Priority
import com.example.todolist.model.Task
import com.example.todolist.ui.activities.ui.theme.TodoListTheme
import com.example.todolist.viewmodel.TaskViewModel

class TaskViewActivity : ComponentActivity() {

    var task: Task = Task()
    var taskViewModel: TaskViewModel = TaskViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        task = intent.getSerializableExtra("task") as Task

        Log.d("AnhHuy", task.name)

        setContent {
            TodoListTheme {
                TaskViewScreen(task)
            }
        }
    }

    @Composable
    private fun TaskViewScreen(
        task: Task
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background("#3D61C7".color)
        ) {
            AppTopBar()

            ContentDetail(task)
        }
    }

    @Preview(showSystemUi = true, showBackground = true)
    @Composable
    private fun ContentDetail(
        task: Task = Task(
            name = "Finish the others function for Todo List app",
            description = "Finish the others function for Todo List app",
            priority = Priority.HIGH
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize()
                .padding(top = 100.dp)
                ,
            elevation = 20.dp,
            backgroundColor = Color.White,
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 80.dp, height = 5.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.Gray)
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 30.dp)
                )

                Text(
                    text = task.name,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(start = 10.dp, top = 40.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, start = 20.dp, end = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier
                            .wrapContentSize()
                    ) {
                        Text(
                            text = "Assigned to",
                            fontSize = 15.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(bottom = 10.dp)
                        )

                        Row(
                            modifier = Modifier
                                .wrapContentSize()
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.galileo),
                                contentDescription = "avatar",
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(androidx.compose.foundation.shape.CircleShape),
                                contentScale = ContentScale.Crop
                            )

                            Text(
                                text = "Galileo",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(start = 10.dp)
                                    .align(Alignment.CenterVertically)
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .wrapContentSize()
                    ) {
                        Text(
                            text = "Due Date",
                            fontSize = 15.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(bottom = 10.dp)
                        )

                        Text(
                            text = task.convertTimeToDate(task.timeStamp),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(top = 8.dp)
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Priority",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 10.dp)
                    )

                    Row(
                        modifier = Modifier
                            .wrapContentSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = when (task.priority) {
                                Priority.HIGH -> "HIGH"
                                Priority.MEDIUM -> "MEDIUM"
                                Priority.LOW -> "LOW"
                            },
                            fontSize = 17.sp,
                            modifier = Modifier
                                .padding(end = 15.dp)
                        )

                        CircleShape(colorRes = when (task.priority) {
                            Priority.HIGH -> R.color.red
                            Priority.MEDIUM -> R.color.green
                            Priority.LOW -> R.color.black
                        })
                    }
                }

                Text(
                    text = "Description",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp)
                )

                Text(
                    text = task.description,
                    color = Color.Gray,
                    fontSize = 15.sp,
                    modifier = Modifier
                        .padding(20.dp)
                )
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

    @Preview(showBackground = true)
    @Composable
    private fun AppTopBar() {
        var expanded by remember { mutableStateOf(false) }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            color = "#3D61C7".color,
            contentColor = MaterialTheme.colors.onPrimary
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = { onBackPressed() },
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.arrow_back),                        contentDescription = "Back",
                            modifier = Modifier
                                .size(30.dp)
                                .align(Alignment.CenterVertically)
                                .clickable {
                                    onBackPressed()
                                }
                        )
                    }

                    Text(
                        text = "Task Detail",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                    )

                    IconButton(
                        onClick = { expanded = !expanded },
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.menu),                        contentDescription = "Back",
                            modifier = Modifier
                                .size(30.dp)
                                .align(Alignment.CenterVertically)
                                .clickable {
                                    expanded = !expanded
                                }
                        )
                    }
                }

                if (expanded) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                expanded = false
                            }
                            .wrapContentSize(Alignment.TopEnd)
                            .padding(end = 20.dp)
                    ) {
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            DropdownMenuItem(onClick = {
                                val editIntent = Intent(this@TaskViewActivity, AddTaskActivity::class.java)
                                editIntent.putExtra("action", "edit")
                                editIntent.putExtra("task", task)
                                startActivity(editIntent)
                                finish()
                            }) {
                                Text(text = "Edit")
                            }

                            DropdownMenuItem(onClick = {
                                taskViewModel.deleteTask(task)
                                val intent: Intent = Intent(this@TaskViewActivity, TodoListActivity::class.java)
                                intent.putExtra("action", "reload")
                                startActivity(intent)
                                finish()
                            }) {
                                Text(text = "Delete")
                            }
                        }
                    }
                }
            }
        }
    }

    private val String.color
        get() = Color(android.graphics.Color.parseColor(this))
}


