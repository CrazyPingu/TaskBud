package com.mobile.todo.database.converter

import androidx.room.TypeConverter
import java.sql.Date

class DateTypeConverter {
    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return timestamp?.let { Date(it) }
    }

    @TypeConverter
    fun toTimestamp(date: java.sql.Date?): Long? {
        return date?.time
    }
}
