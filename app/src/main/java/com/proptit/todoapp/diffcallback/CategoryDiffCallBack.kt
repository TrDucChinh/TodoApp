package com.proptit.todoapp.diffcallback

import androidx.recyclerview.widget.DiffUtil
import com.proptit.todoapp.model.Category

class CategoryDiffCallBack : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        TODO("Not yet implemented")
    }
}