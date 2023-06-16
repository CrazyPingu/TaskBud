package com.mobile.todo.database.dataset

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
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
data class Habit(
    val title: String,
    val description: String,
    val userId: Int,
    val streak: Int = 0,
    val isCompleted: Boolean = false,
    val lastDayCompleted : Date? = null,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
)
