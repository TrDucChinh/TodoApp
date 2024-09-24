package com.proptit.todoapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.proptit.todoapp.database.TodoDatabase
import com.proptit.todoapp.database.category.CategoryRepository
import com.proptit.todoapp.model.Category
import com.proptit.todoapp.utils.List

class CategoryViewModel(application: Application) : ViewModel() {
    private val categoryRepository: CategoryRepository = CategoryRepository(application, List.categoryItems)

    fun getAllCategory() = categoryRepository.getAllCategory()
    suspend fun insertCategory(category: Category) = categoryRepository.insertCategory(category)
    suspend fun deleteCategory(category: Category) = categoryRepository.deleteCategory(category)
    suspend fun updateCategory(category: Category) = categoryRepository.updateCategory(category)

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