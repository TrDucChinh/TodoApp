package com.proptit.todoapp.database.subtask

import android.app.Application
import com.proptit.todoapp.database.TodoDatabase
import com.proptit.todoapp.model.Subtask

class SubTaskRepository(application : Application) {
    private val subTaskDao: SubTaskDao = TodoDatabase.getDatabase(application).subTaskDao()

    suspend fun insertSubTask(subTask: Subtask) = subTaskDao.insertSubtask(subTask)
    suspend fun updateSubTask(subTask: Subtask) = subTaskDao.updateSubtask(subTask)
    suspend fun deleteSubTask(subTask: Subtask) = subTaskDao.deleteSubtask(subTask)
    fun getAllSubTask() = subTaskDao.getAllSubtasks()
    fun getSubTaskByTaskId(taskId: Int) = subTaskDao.getSubtasksByTaskId(taskId)
    suspend fun deleteSubTaskByTaskId(taskId: Int) = subTaskDao.deleteSubtasksByTaskId(taskId)
}