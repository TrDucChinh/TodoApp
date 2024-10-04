package com.proptit.todoapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category")
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val titleCategory: String,
    val idColor: Int,
    val idIcon : Int
)