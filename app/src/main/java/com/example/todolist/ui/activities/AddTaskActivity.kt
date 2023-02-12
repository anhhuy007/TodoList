package com.example.todolist.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.example.todolist.R
import com.example.todolist.model.Priority
import com.example.todolist.ui.data.AddingTaskUiState
import com.example.todolist.ui.theme.TodoListTheme
import com.example.todolist.viewmodel.AddTaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.asStateFlow

class AddTaskActivity : ComponentActivity() {
    private val viewModel: AddTaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TodoListTheme {
                AddTaskContent(viewModel.addingTaskUiState.collectAsState().value)
            }
        }
    }

    @Composable
    private fun AddTaskContent(
        uiState: AddingTaskUiState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Text(
                text = stringResource(R.string.add_task),
                style = MaterialTheme.typography.h1,
                modifier = Modifier.padding(8.dp)
            )

            OutlinedTextField(
                label = {Text(text = stringResource(R.string.task_title))},
                value = uiState.title,
                onValueChange = { viewModel.updateTitle(it) },
                modifier = Modifier
                    .fillMaxWidth()
            )

            DropDownPriorityMenu(uiState = uiState)

            OutlinedTextField(
                label = {Text(text = stringResource(R.string.task_description))},
                value = uiState.description,
                onValueChange = { viewModel.updateDescription(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            Buttons(uiState = uiState)
        }
    }

    @Composable
    private fun Buttons(uiState: AddingTaskUiState) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { onBackPressed() },
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Text(text = stringResource(R.string.cancel))
            }

            Spacer(Modifier.width(30.dp))

            Button(
                onClick = {
                    viewModel.onAddTask()
                    val intent = Intent(this@AddTaskActivity, TodoListActivity::class.java)
                    intent.putExtra("action", "reload")
                    startActivity(intent)
                    finish()
                },
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Text(text = stringResource(R.string.add_task))
            }
        }
    }

    @Composable
    private fun DropDownPriorityMenu(uiState: AddingTaskUiState) {
        var expanded by remember { mutableStateOf(false) }
        var selectedItem by remember { mutableStateOf(Priority.HIGH) }
        val priorityList = listOf(Priority.HIGH, Priority.MEDIUM, Priority.LOW)
        var textFiledSize by remember { mutableStateOf(Size.Zero) }

        val icon = if (expanded) {
            Icons.Filled.KeyboardArrowUp
        } else {
            Icons.Filled.KeyboardArrowDown
        }

        Column {
            OutlinedTextField(
                value = selectedItem.toString(),
                onValueChange = { },
                enabled = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        textFiledSize = coordinates.size.toSize()
                    },
                label = { Text(text = "Select priority") },
                leadingIcon = {
                    CircleShape(colorRes = when (selectedItem) {
                        Priority.HIGH -> R.color.red
                        Priority.MEDIUM -> R.color.green
                        Priority.LOW -> R.color.black
                    })
                },
                trailingIcon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = "Select priority",
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable { expanded = !expanded }
                    )
                },
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.width(with(LocalDensity.current) { textFiledSize.width.toDp() })
            ) {
                priorityList.forEach { priority ->
                    DropdownMenuItem(
                        onClick = {
                            selectedItem = priority
                            expanded = false
                            viewModel.updatePriority(priority)
                            Log.d("AnhHuy", "Priority: ${uiState.priority}")
                        }
                    ) {
                        Text(
                            text = priority.toString(),
                        )
                    }
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    private fun AddTaskContentPreview() {
        AddTaskContent(uiState = AddingTaskUiState())
    }

    private fun stringToPriority(it: String): Priority {
        return when (it) {
            "HIGH" -> Priority.HIGH
            "MEDIUM" -> Priority.MEDIUM
            "LOW" -> Priority.LOW
            else -> Priority.HIGH
        }
    }
}
