package com.mobile.todo.database.dataset

import androidx.room.*
import com.mobile.todo.database.converter.DateTypeConverter
import java.util.Date

@Entity(
    foreignKeys = [ForeignKey(
        entity = Tag::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("tagId"),
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    ),
    ForeignKey(
        entity = User::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("userId"),
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
    val tagId: Int,
    val userId: Int,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
)

