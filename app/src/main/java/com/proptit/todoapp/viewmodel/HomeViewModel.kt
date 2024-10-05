package com.proptit.todoapp.viewmodel

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.proptit.todoapp.database.task.TaskRepository
import com.proptit.todoapp.model.Task
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class HomeViewModel(application: Application) : ViewModel() {
    private val taskRepository: TaskRepository = TaskRepository(application)

    fun getAllTask() = taskRepository.getAllTask()
    fun getAllTaskToday() = taskRepository.getAllTaskToday()
    fun getAllTaskTomorrow() = taskRepository.getAllTaskTomorrow()
    fun getAllTaskWeek() = taskRepository.getAllTaskWeek()
    fun getAllTaskMonth() = taskRepository.getAllTaskMonth()

    fun updateTask(task: Task) {
        viewModelScope.launch {
            taskRepository.updateTask(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskRepository.deleteTask(task)
        }
    }

    fun getTaskById(id: Long) = taskRepository.getTaskById(id)

    fun getAllCompletedTasks() = taskRepository.getAllCompletedTasks()
    fun getAllUncompletedTasks() = taskRepository.getAllUncompletedTasks()

    class HomeViewModelFactory(private val application: Application) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return HomeViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
