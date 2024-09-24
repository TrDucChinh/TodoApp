package com.proptit.todoapp.interfaces

import com.proptit.todoapp.model.Category

interface ICategoryListener {
    fun onCategoryClick(category: Category)
    fun onAddCategoryClick()
}