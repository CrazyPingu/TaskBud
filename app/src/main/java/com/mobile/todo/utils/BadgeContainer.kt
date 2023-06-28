package com.mobile.todo.utils

import android.net.Uri

data class BadgeContainer(
    val name: String,
    val description: String,
    val streak_bp: Int?,
    val icon: Uri,
    val obtained : Boolean,
)
