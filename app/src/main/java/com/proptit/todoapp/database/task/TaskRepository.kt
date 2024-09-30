package com.proptit.todoapp.database.task

import android.app.Application
import android.icu.text.CaseMap.Title
import com.proptit.todoapp.database.TodoDatabase
import com.proptit.todoapp.model.Task
import java.util.Date

class TaskRepository(application: Application) {
    private val taskDao: TaskDao = TodoDatabase.getDatabase(application).taskDao()

    suspend fun insertTask(
        title: String,
        description: String,
        dueDate: Date,
        dueTime: Date,
        categoryId: Int,
        isFinish: Boolean,
        taskPriority: Int
        ): Long {
        val task = Task(
            title = title,
            description = description,
            dueDate = dueDate,
            dueTime = dueTime,
            categoryId = categoryId,
            isFinish = isFinish,
            taskPriority = taskPriority
        )
        return taskDao.insertTask(task)
    }


    fun getAllTask() = taskDao.getAllTask()
    fun getAllTaskToday() = taskDao.getAllTaskToday()
    fun getAllTaskTomorrow() = taskDao.getAllTaskTomorrow()
    fun getAllCompletedTasks() = taskDao.getAllCompletedTasks()
    fun getAllUncompletedTasks() = taskDao.getAllUncompletedTasks()
}