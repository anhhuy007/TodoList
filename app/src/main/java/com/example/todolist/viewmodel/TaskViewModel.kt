package com.example.todolist.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.load.HttpException
import com.example.todolist.data.NetworkTaskRepository
import com.example.todolist.data.TaskRepository
import com.example.todolist.model.Response
import com.example.todolist.model.Task
import com.example.todolist.network.FirebaseCallback
import com.example.todolist.ui.data.AddingTaskUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.IOError
import java.io.IOException

sealed class TaskUiState {
    data class Success(val tasks: List<Task>) : TaskUiState()
    data class Error(val error: String) : TaskUiState()
    object Loading : TaskUiState()
}

class TaskViewModel(
    private val taskRepository: TaskRepository = NetworkTaskRepository()
) : ViewModel() {
    var taskUiState: TaskUiState by mutableStateOf(TaskUiState.Loading)
        private set

    var addingTaskUiState: AddingTaskUiState by mutableStateOf(AddingTaskUiState())
        private set

    init {
        getTasks()
    }

    fun getTasks() {
        viewModelScope.launch {
            taskUiState = TaskUiState.Loading
            taskUiState = try {
                taskRepository.getTasks(object : FirebaseCallback {
                    override fun onRespond(response: Response) {
                        response.tasks?.let { TaskUiState.Success(it) }
                        Log.d("AnhHuy", "On success loading data")
                        taskUiState = response.tasks?.let { TaskUiState.Success(it) }!!
                    }
                })

                TaskUiState.Loading
            } catch (e: IOException) {
                TaskUiState.Error(e.message ?: "Unknown error")
            } catch (e: HttpException) {
                TaskUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}