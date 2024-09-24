package com.proptit.todoapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String = "",
    val subtasks: List<Subtask> = emptyList(),
    val dateCreated: String,
    val dueDate: String = "",
    val dueTime: String = "",
    val titleCategory: String,
    val isFinish: Boolean = false,
    val taskPriority: Int = 0,
)