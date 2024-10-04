package com.proptit.todoapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.proptit.todoapp.database.task.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

class AddTaskViewModel(application: Application): ViewModel() {
    private val taskRepository: TaskRepository = TaskRepository(application)

    fun insertTask(
        title: String,
        description: String,
        dueDate: Date,
        dueTime: Date,
        categoryId: Int,
        isFinish: Boolean,
        taskPriority: Int
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.insertTask(title, description, dueDate, dueTime, categoryId, isFinish, taskPriority)
        }
    }

    class AddTaskViewModelFactory(private val application: Application) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AddTaskViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AddTaskViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}