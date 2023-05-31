package com.example.todolist.data

import android.util.Log
import com.example.todolist.model.Response
import com.example.todolist.model.Task
import com.example.todolist.network.FirebaseCallback
import com.google.firebase.database.*

interface TaskRepository {
    suspend fun getTasks(firebaseCallback: FirebaseCallback)
    suspend fun addTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(task: Task)
    suspend fun deleteAllTasks()
}

class NetworkTaskRepository(
    private val databaseRef: DatabaseReference = FirebaseDatabase.getInstance().reference,
    private val taskRef: DatabaseReference = databaseRef.child("tasks")
) : TaskRepository {
    override suspend fun getTasks(firebaseCallback: FirebaseCallback) {
        taskRef.get().addOnCompleteListener { task ->
            val response = Response()
            if (task.isSuccessful) {
                val result = task.result
                result?.let {
                    response.tasks = result.children.map { snapShot ->
                        snapShot.getValue(Task::class.java)!!
                    }
                }
            } else {
                response.error = task.exception
                Log.d("AnhHuy", "getTasks: ${task.exception}")
            }
            firebaseCallback.onRespond(response)
        }
    }

    override suspend fun addTask(task: Task) {
        taskRef.push().setValue(task)
    }

    override suspend fun updateTask(task: Task) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTask(task: Task) {
        taskRef.addValueEventListener(object : ValueEventListener {
             override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    val task_ = data.getValue(Task::class.java)
                    if (task_ == task) {
                        data.ref.removeValue()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("AnhHuy", "onCancelled: ${error.message}")
            }
        })
    }

    override suspend fun deleteAllTasks() {
        TODO("Not yet implemented")
    }
}