package com.mobile.todo.database.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.mobile.todo.database.dataset.Badge

interface BadgeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertBadge(badge: Badge)
}