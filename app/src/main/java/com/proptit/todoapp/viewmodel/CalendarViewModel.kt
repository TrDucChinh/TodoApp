package com.proptit.todoapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proptit.todoapp.database.task.TaskRepository
import com.proptit.todoapp.model.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

class CalendarViewModel(application: Application) : ViewModel() {
    private val taskRepository: TaskRepository = TaskRepository(application)
    fun getTaskByDate(selectedDate: Date) = taskRepository.getTaskByDate(selectedDate)
    fun updateTask(task : Task){
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.updateTask(task)
        }
    }
    class CalendarViewModelFactory(private val application: Application) : androidx.lifecycle.ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CalendarViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CalendarViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}