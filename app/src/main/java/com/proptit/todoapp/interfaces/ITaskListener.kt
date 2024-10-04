package com.proptit.todoapp.interfaces

import com.proptit.todoapp.model.Task

interface ITaskListener {
    fun onTask(taskId: Long)
    fun onTaskStatusChange(task: Task)


}