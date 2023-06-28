package com.mobile.todo.adapter

import android.net.Uri

data class BadgeContainer(
    val name: String,
    val description: String,
    val streak_bp: Int,
    val type: Boolean,
    val icon: Uri,
    val id : Int,
    val obtained : Boolean,
)
