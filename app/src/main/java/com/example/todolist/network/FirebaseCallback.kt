package com.example.todolist.network

import com.example.todolist.model.Response

interface FirebaseCallback {
    fun onRespond(response: Response)
}