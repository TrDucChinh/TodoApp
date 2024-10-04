package com.proptit.todoapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.proptit.todoapp.database.category.CategoryRepository
import com.proptit.todoapp.utils.ListData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreateCategoryViewModel(application: Application): ViewModel() {
    private val categoryRepository: CategoryRepository = CategoryRepository(application, ListData.categoryItems)

     fun insertCategory(titleCategory: String, idColor: Int, idIcon: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            categoryRepository.insertCategory(titleCategory, idColor, idIcon)
        }

    }
    class CreateCategoryViewModelFactory(private val application: Application) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CreateCategoryViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CreateCategoryViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}