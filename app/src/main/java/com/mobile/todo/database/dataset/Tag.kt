package com.mobile.todo.database.dataset

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Tag(
    val name: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
)
