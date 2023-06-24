package com.mobile.todo.database.dataset

import androidx.room.*

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = ToDo::class,
            parentColumns = ["id"],
            childColumns = ["toDoId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Tag::class,
            parentColumns = ["tag"],
            childColumns = ["tag"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["toDoId"]),
        Index(value = ["tag"])
    ]
)
data class Search(
    val toDoId: Int,
    val tag: String,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
)

