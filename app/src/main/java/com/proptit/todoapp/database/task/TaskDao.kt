package com.proptit.todoapp.database.task

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.proptit.todoapp.model.Task
import java.util.Date

@Dao
interface TaskDao {

    @Insert
    suspend fun insertTask(task: Task)

    @Query("SELECT * FROM task")
    fun getAllTask(): LiveData<List<Task>>

//    @Query("SELECT * FROM task WHERE dueDate >= strftime('%s', 'now', 'start of day') * 1000 AND dueDate < strftime('%s', 'now', '+1 day', 'start of day') * 1000 AND isFinish = 0")
    @Query(" SELECT * FROM task WHERE date(dueDate / 1000, 'unixepoch') = date('now', 'localtime') AND isFinish = 0 ORDER BY dueDate ASC, dueTime ASC")
    fun getAllTaskToday(): LiveData<List<Task>>


//    @Query("SELECT * FROM task WHERE dueDate >= strftime('%s', 'now', '+1 day', 'start of day') * 1000 AND dueDate < strftime('%s', 'now', '+2 days', 'start of day') * 1000 AND isFinish = 0")
    @Query("SELECT * FROM task WHERE date(dueDate / 1000, 'unixepoch') = date('now', 'localtime', '+1 day') AND isFinish = 0 ORDER BY dueDate ASC, dueTime ASC")
    fun getAllTaskTomorrow(): LiveData<List<Task>>


    @Query("SELECT * FROM task WHERE date(dueDate / 1000, 'unixepoch') BETWEEN date('now', 'weekday 1', '-7 days') AND date('now', 'weekday 0', '+0 days') AND isFinish = 0 ORDER BY dueDate ASC, dueTime ASC")
    fun getAllTaskThisWeek(): LiveData<List<Task>>



    @Query("SELECT * FROM task WHERE date(dueDate / 1000, 'unixepoch') BETWEEN date('now', 'start of month') AND date('now', 'start of month', '+1 month', '-1 day') AND isFinish = 0 ORDER BY dueDate ASC, dueTime ASC")
    fun getAllTaskThisMonth(): LiveData<List<Task>>


    @Query("SELECT * FROM task WHERE isFinish = 1 ORDER BY dueDate ASC, dueTime ASC")
    fun getAllCompletedTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE isFinish = 0 ORDER BY dueDate ASC, dueTime ASC")
    fun getAllUncompletedTasks(): LiveData<List<Task>>

    @Update
    suspend fun updateTask(task: Task)


}