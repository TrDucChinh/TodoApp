package com.proptit.todoapp.interfaces

import com.proptit.todoapp.model.Task

interface IAddTaskListener {
    fun onAddTask(task : Task)
}