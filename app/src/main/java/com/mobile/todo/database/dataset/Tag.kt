package com.mobile.todo.database.dataset

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Tag(
    @PrimaryKey val tag: String
) {
    companion object {
        const val FAV = "favorites"

        const val SHOW_ALL = "show all"
    }
}
