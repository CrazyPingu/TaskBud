package com.mobile.todo.database.dataset

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Badge(
    val name: String,
    val description: String,
    val streak_bp: Int,
    // 0/false = Todo, 1/true = habit
    val type: Boolean,
    val icon: Uri,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
)



