package com.proptit.todoapp.database.category

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.proptit.todoapp.model.Category

@Dao
interface CategoryDao {
    @Query("SELECT * FROM category")
    fun getAllCategory(): LiveData<List<Category>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category)
    @Delete
    suspend fun deleteCategory(category: Category)
    @Update
    suspend fun updateCategory(category: Category)
}