package com.proptit.todoapp.diffcallback

import androidx.recyclerview.widget.DiffUtil
import com.proptit.todoapp.model.Category
import com.proptit.todoapp.model.Task

class TaskDiffCallBack : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem == newItem
    }
}