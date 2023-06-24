package com.mobile.todo.database.dataset

import androidx.room.*
import com.mobile.todo.database.converter.DateTypeConverter
import java.util.Date

@Entity(
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["userId"],
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )]
)
@TypeConverters(DateTypeConverter::class)
data class ToDo(
    val title: String,
    val description: String,
    val date: Date? = null,
    val completed: Boolean = false,
    val userId: Int,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)
