package com.proptit.todoapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.proptit.todoapp.database.task.TaskRepository
import com.proptit.todoapp.model.Task
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : ViewModel() {
    private val taskRepository: TaskRepository = TaskRepository(application)

    fun getAllTask() = taskRepository.getAllTask()
    fun getAllTaskToday() = taskRepository.getAllTaskToday()
    fun getAllTaskTomorrow() = taskRepository.getAllTaskTomorrow()
    fun getAllTaskWeek() = taskRepository.getAllTaskWeek()
    fun getAllTaskMonth() = taskRepository.getAllTaskMonth()

    fun updateTask(task: Task){
        viewModelScope.launch {
            taskRepository.updateTask(task)
        }
    }

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