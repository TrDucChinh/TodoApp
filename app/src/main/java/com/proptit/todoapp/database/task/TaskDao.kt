package com.proptit.todoapp.database.task

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.proptit.todoapp.model.Task
@Dao
interface TaskDao{

    @Insert
    suspend fun insertTask(task: Task): Long

    @Query("SELECT * FROM task")
    fun getAllTask(): LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE dueDate = DATE('now')")
    fun getAllTaskToday(): LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE dueDate BETWEEN DATE('now', 'weekday 0', '-6 days') AND DATE('now', 'weekday 0')")
    fun getAllTaskWeek(): LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE dueDate BETWEEN DATE('now', 'start of month') AND DATE('now', 'start of month', '+1 month', '-1 day')")
    fun getAllTaskMonth(): LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE isFinish = 1")
    fun getAllCompletedTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE isFinish = 0")
    fun getAllUncompletedTasks(): LiveData<List<Task>>


}