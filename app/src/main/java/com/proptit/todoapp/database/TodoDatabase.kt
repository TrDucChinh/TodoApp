package com.proptit.todoapp.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.proptit.todoapp.database.category.CategoryDao
import com.proptit.todoapp.model.Category
import com.proptit.todoapp.model.Task

@Database(entities = [Category::class, Task::class], version = 1, exportSchema = false)
abstract class TodoDatabase : RoomDatabase() {
    //    abstract fun taskDao() : TaskDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        @Volatile
        private var DATABASE_NAME = "todo_database.db"
        private var INSTANCE: TodoDatabase? = null
        fun getDatabase(context: android.content.Context): TodoDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TodoDatabase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}