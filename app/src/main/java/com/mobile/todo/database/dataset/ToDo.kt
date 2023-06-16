package com.mobile.todo.database.dataset

import androidx.room.*
import com.mobile.todo.database.converter.DateTypeConverter
import java.sql.Date

@Entity(
    foreignKeys = [ForeignKey(
        entity = Folder::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("folderId"),
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )]
)
@TypeConverters(DateTypeConverter::class)
data class ToDo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val date: Date,
    val completed: Boolean = false,
    val folderId: Int,
)

