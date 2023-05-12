package com.mobile.todo.database.dataset

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Badge(
    val name: String,
    val description: String,
    val streak_bp: Int,
    val type: Boolean,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
)



