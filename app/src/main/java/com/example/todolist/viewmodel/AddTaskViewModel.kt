package com.example.todolist.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.NetworkTaskRepository
import com.example.todolist.data.TaskRepository
import com.example.todolist.model.Priority
import com.example.todolist.model.Task
import com.example.todolist.ui.data.AddingTaskUiState
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddTaskViewModel(
    private val taskRepository: TaskRepository = NetworkTaskRepository()
) : ViewModel(){
    val addingTaskUiState = MutableStateFlow(AddingTaskUiState())

    fun updateTitle(title: String){
        addingTaskUiState.value = addingTaskUiState.value.copy(title = title)
    }

    fun updatePriority(priority: Priority){
        addingTaskUiState.value = addingTaskUiState.value.copy(priority = priority)
    }

    fun updateDescription(description: String){
        addingTaskUiState.value = addingTaskUiState.value.copy(description = description)
    }

    fun onAddTask() {
        if (addingTaskUiState.value.title.isBlank()) {
            return
        }

        viewModelScope.launch {
             taskRepository.addTask(Task(
                 name = addingTaskUiState.value.title,
                 priority = addingTaskUiState.value.priority,
                 description = addingTaskUiState.value.description))
        }
    }
}