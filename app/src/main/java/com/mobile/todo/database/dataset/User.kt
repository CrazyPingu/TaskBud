package com.mobile.todo.database.dataset

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity
data class User(
    val username: String,
    val password: String,
    val GPS: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
)
