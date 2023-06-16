package com.mobile.todo.database.dataset

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.mobile.todo.database.converter.DateTypeConverter
import java.util.Date

@Entity(
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("userId"),
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )]
)
@TypeConverters(DateTypeConverter::class)
data class Habit(
    val title: String,
    val description: String,
    val userId: Int,
    val streak: Int = 0,
    val lastDayCompleted : Date? = null,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
)
