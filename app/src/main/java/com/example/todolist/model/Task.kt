package com.example.todolist.model

import java.text.SimpleDateFormat
import java.util.*
data class Task(
    var name: String = "",
    var priority: Priority = Priority.LOW,
    var description: String = "",
    var timeStamp: Long = Calendar.getInstance().timeInMillis
) : java.io.Serializable {
    fun convertTimeToDate(timeStamp: Long) : String {
        val dateFormat = SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault())
        return dateFormat.format(Date(timeStamp))
    }
}

enum class Priority {
    HIGH, MEDIUM, LOW
}


