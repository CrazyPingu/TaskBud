package com.mobile.todo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mobile.todo.adapter.BadgeContainer
import com.mobile.todo.database.dataset.Badge

@Dao
interface BadgeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertBadge(badge: Badge)

    @Query("SELECT * FROM Badge")
    fun getAllBadge() : List<Badge>

    @Query("SELECT Badge.*, UserBadge.obtained " +
            "FROM Badge INNER JOIN UserBadge ON Badge.id = UserBadge.badgeId " +
            "WHERE UserBadge.userId = :userId")
    fun getAllBadgeFromUser(userId: Int): List<BadgeContainer>

}