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
            entity = ToDo::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("todoId"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )]
)
@TypeConverters(DateTypeConverter::class)
data class Search(
    val tagId: Int,
    val todoId: Int,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
)

