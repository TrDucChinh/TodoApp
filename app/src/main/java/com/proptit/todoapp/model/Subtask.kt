package com.proptit.todoapp.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "subtask",
    foreignKeys = [
        ForeignKey(
            entity = Task::class,
            parentColumns = ["id"],
            childColumns = ["taskId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Subtask(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var title: String,
    var taskId: Int,
    var isFinish : Boolean
)