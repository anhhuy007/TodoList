package com.example.todolist.ui.data

import com.example.todolist.model.Priority

data class AddingTaskUiState(
    val title: String = "",
    val priority: Priority = Priority.LOW,
    val description: String = ""
)