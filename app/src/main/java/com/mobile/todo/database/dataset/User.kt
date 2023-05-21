package com.mobile.todo.database.dataset

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    val username: String,
    val password: String,
    val city: String,
    val ProfilePic: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
)
