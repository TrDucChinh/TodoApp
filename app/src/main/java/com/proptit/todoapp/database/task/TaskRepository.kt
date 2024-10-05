package com.proptit.todoapp.database.task

import android.app.Application
import android.icu.text.CaseMap.Title
import androidx.lifecycle.LiveData
import com.proptit.todoapp.database.TodoDatabase
import com.proptit.todoapp.model.Task
import java.util.Calendar
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
        taskPriority: Int,
    ) {
        val task = Task(
            title = title,
            description = description,
            dueDate = dueDate,
            dueTime = dueTime,
            categoryId = categoryId,
            isFinish = isFinish,
            taskPriority = taskPriority
        )

        taskDao.insertTask(task)
    }

    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
    }

    suspend fun deleteTask(task: Task) = taskDao.deleteTask(task)

    fun getTaskByDate(selectedDate: Date) = taskDao.getTaskByDate(selectedDate)

    fun getTaskById(taskId: Long) = taskDao.getTaskById(taskId)

    fun getAllTask() = taskDao.getAllTask()
    fun getAllTaskTomorrow() = taskDao.getAllTaskTomorrow()
    fun getAllTaskToday() = taskDao.getAllTaskToday()
    fun getAllTaskWeek() = taskDao.getAllTaskThisWeek()


    fun getAllTaskMonth() = taskDao.getAllTaskThisMonth()
    fun getAllCompletedTasks() = taskDao.getAllCompletedTasks()
    fun getAllUncompletedTasks() = taskDao.getAllUncompletedTasks()

    suspend fun getAllUncompletedTasksDirectly(): List<Task> {
        return taskDao.getAllUncompletedTasksDirectly()
    }

}