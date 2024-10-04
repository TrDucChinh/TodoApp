package com.proptit.todoapp.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.proptit.todoapp.model.Subtask
import java.util.Date

class Converters {
    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }
    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return timestamp?.let { Date(it) }
    }
    @TypeConverter
    fun fromJson(jsonString: String): List<Subtask> {
        val listType = object : TypeToken<List<Subtask>>() {}.type
        return Gson().fromJson(jsonString, listType)
    }

    @TypeConverter
    fun fromStringList(list: List<Subtask>): String = Gson().toJson(list)
}