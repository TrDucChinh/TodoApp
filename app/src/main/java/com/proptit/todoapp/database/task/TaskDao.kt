package com.proptit.todoapp.database.task

import androidx.lifecycle.LiveData
import androidx.room.Query
import com.proptit.todoapp.model.Task

interface TaskDao{
    @Query("SELECT * FROM task")
    fun getAllTask(): LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE dueDate = DATE('now')")
    fun getAllTaskToday(): LiveData<List<Task>>

    @
}