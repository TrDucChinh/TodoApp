package com.proptit.todoapp.interfaces

import com.proptit.todoapp.model.Task

interface ITaskListener {
    fun onTask(task : Task)
    fun onTaskStatusChange(task: Task)


}