package com.proptit.todoapp.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.proptit.todoapp.database.task.TaskRepository
import com.proptit.todoapp.model.Subtask
import com.proptit.todoapp.model.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailTaskViewModel(private val task: Task) : ViewModel() {
    var newTitle = task.title
    var newDescription = task.description
    var newDueDate = task.dueDate
    var newDueTime = task.dueTime
    var newIsFinished = task.isFinish
    var newCategoryId = task.categoryId
    var newTaskPriority = task.taskPriority

    private var newSubtasks = task.subTask.toMutableList()
    private val subTasks = MutableLiveData<List<Subtask>>()
    val _subTasks
        get() = subTasks

    fun isChanged(): Boolean {
        return newTitle != task.title ||
                newDescription != task.description ||
                newDueDate != task.dueDate ||
                newDueTime != task.dueTime ||
                newIsFinished != task.isFinish ||
                newCategoryId != task.categoryId ||
                newTaskPriority != task.taskPriority ||
                subTasksChanged()
    }

    private fun subTasksChanged(): Boolean {
        if (newSubtasks.size != task.subTask.size)
            return true
        for (i in newSubtasks.indices) {
            if ((newSubtasks[i].title != task.subTask[i].title) || (newSubtasks[i].isFinish != task.subTask[i].isFinish))
                return true
        }
        return false
    }

    fun getNewTask(): Task {
        return task.copy(
            title = newTitle,
            description = newDescription,
            dueDate = newDueDate,
            dueTime = newDueTime,
            subTask = newSubtasks,
            isFinish = newIsFinished,
            categoryId = newCategoryId,
            taskPriority = newTaskPriority
        )
    }

    fun addSubtask(subtask: Subtask) {
        newSubtasks.add(subtask)
        subTasks.value = newSubtasks
    }

    fun onUpdatedSubtask(position: Int) {
        val isFinish = !newSubtasks[position].isFinish
        newSubtasks[position] = newSubtasks[position].copy(isFinish = isFinish)
        subTasks.value = newSubtasks
    }

    fun onRemoveSubtask(position: Int) {
        newSubtasks.removeAt(position)
        subTasks.value = newSubtasks
    }

    class DetailTaskViewModelFactory(private val task: Task) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DetailTaskViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DetailTaskViewModel(task) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    /*private val taskRepository: TaskRepository = TaskRepository(application)
    fun updateTask(task: Task){
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.updateTask(task)
        }
    }

    fun getTaskById(taskId: Long) = taskRepository.getTaskById(taskId)

    fun deleteTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.deleteTask(task)
        }
    }

    class DetailTaskViewModelFactory(private val application :Application) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DetailTaskViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DetailTaskViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }*/

}