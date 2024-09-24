package com.proptit.todoapp.database.category

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.proptit.todoapp.database.TodoDatabase
import com.proptit.todoapp.model.Category

class CategoryRepository(
    application: Application,
    private val localItem: List<Category>
) {
    private val categoryDao: CategoryDao = TodoDatabase.getDatabase(application).categoryDao()

    private fun getAllCategoryFromDB() : LiveData<List<Category>> {
        return categoryDao.getAllCategory()
    }
    private fun getDateFromLocal() : LiveData<List<Category>> {
        return MutableLiveData(localItem)
    }

    fun getAllCategory() : LiveData<List<Category>> {
        val result = MediatorLiveData<List<Category>>()
        val databaseCategory = getAllCategoryFromDB()
        val localCategory = getDateFromLocal()
        result.addSource(databaseCategory) {dbData ->
            val combineData = dbData + localItem
            result.value = combineData
        }
        return result
    }

    suspend fun insertCategory(category: Category) {
        categoryDao.insertCategory(category)
    }
    suspend fun deleteCategory(category: Category) {
        categoryDao.deleteCategory(category)
    }
    suspend fun updateCategory(category: Category) {
        categoryDao.updateCategory(category)
    }

}