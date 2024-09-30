package com.proptit.todoapp.database.task

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.Query
import com.proptit.todoapp.model.Task

interface TaskDao{

    @Insert
    suspend fun insertTask(task: Task): Long

    @Query("SELECT * FROM task")
    fun getAllTask(): LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE dueDate = DATE('now')")
    fun getAllTaskToday(): LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE dueDate = DATE('now', '+1 day')")
    fun getAllTaskTomorrow(): LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE isFinish = 1")
    fun getAllCompletedTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE isFinish = 0")
    fun getAllUncompletedTasks(): LiveData<List<Task>>


}