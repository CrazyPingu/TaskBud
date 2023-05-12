package com.mobile.todo.database.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.mobile.todo.database.dataset.UserBadge

interface UserBadgeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertBadge(userBadge: UserBadge)
}