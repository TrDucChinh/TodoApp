package com.proptit.todoapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.proptit.todoapp.database.category.CategoryRepository
import com.proptit.todoapp.utils.ListData

class CategoryViewModel(application: Application) : ViewModel() {
    private val categoryRepository: CategoryRepository = CategoryRepository(application, ListData.categoryItems)

    fun getAllCategory() = categoryRepository.getAllCategory()

    class CategoryViewModelFactory(private val application: Application) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CategoryViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CategoryViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}