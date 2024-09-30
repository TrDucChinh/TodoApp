package com.proptit.todoapp.database.subtask

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.proptit.todoapp.model.Subtask

@Dao
interface SubTaskDao{
    @Insert
    suspend fun insertSubtask(subtask: Subtask)

    @Update
    suspend fun updateSubtask(subtask: Subtask)

    @Delete
    suspend fun deleteSubtask(subtask: Subtask)

    @Query("SELECT * FROM subtask")
    fun getAllSubtasks(): LiveData<List<Subtask>>

    @Query("SELECT * FROM subtask WHERE taskId = :taskId")
    fun getSubtasksByTaskId(taskId: Int): LiveData<List<Subtask>>

    @Query("DELETE FROM subtask WHERE taskId = :taskId")
    suspend fun deleteSubtasksByTaskId(taskId: Int)
}