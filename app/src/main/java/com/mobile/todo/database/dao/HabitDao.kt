package com.mobile.todo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mobile.todo.database.dataset.Habit

@Dao
interface HabitDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFolder(habit: Habit)

    @Query("SELECT * FROM habit WHERE userId = :userId")
    fun getHabits(userId: Int) : List<Habit>
}