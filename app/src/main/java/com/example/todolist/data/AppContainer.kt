package com.example.todolist.data

interface Appcontainer {
    val taskRepository: TaskRepository
}

class DefaultAppContainer : Appcontainer {
    override val taskRepository: TaskRepository by lazy {
        NetworkTaskRepository()
    }
}
