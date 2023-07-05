package com.mobile.todo.database.dataset

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.mobile.todo.database.converter.UriTypeConverter

@Entity
@TypeConverters(UriTypeConverter::class)
data class User(
    val username: String,
    val password: String,
    val city: String,
    val profilePic: Uri,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
)

