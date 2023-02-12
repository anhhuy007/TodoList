package com.example.todolist.model

import java.util.*

data class Task(
    var name: String = "",
    var priority: Priority = Priority.LOW,
    var description: String = "",
    var timeStamp: Long = Calendar.getInstance().timeInMillis
)

enum class Priority {
    HIGH, MEDIUM, LOW
}


