package com.mobile.todo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mobile.todo.database.dataset.UserBadge

@Dao
interface UserBadgeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertBadge(userBadge: UserBadge)

    @Query("SELECT * FROM UserBadge WHERE userId = :userId")
    fun getAllUserBadge(userId : Int) : List<UserBadge>
}