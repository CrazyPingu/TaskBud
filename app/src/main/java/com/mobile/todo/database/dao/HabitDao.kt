package com.mobile.todo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface HabitDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFolder(habitDao: HabitDao)
}