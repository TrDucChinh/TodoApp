package com.proptit.todoapp.database.category

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.proptit.todoapp.model.Category

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category)
    @Delete
    suspend fun deleteCategory(category: Category)
    @Update
    suspend fun updateCategory(category: Category)
}