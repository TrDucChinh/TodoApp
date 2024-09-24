package com.proptit.todoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.proptit.todoapp.database.category.CategoryRepository

class CategoryViewModel() : ViewModel() {


    class CategoryViewModelFactory(private val repository: CategoryRepository) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CategoryViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CategoryViewModel() as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}