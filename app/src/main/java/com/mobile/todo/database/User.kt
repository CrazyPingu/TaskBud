package com.mobile.todo.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    val username: String,
    val password: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
)