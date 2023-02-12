package com.example.todolist.model

data class Response(
    var tasks: List<Task> ?= null,
    var error: java.lang.Exception ?= null
)