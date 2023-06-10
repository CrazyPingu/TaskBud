package com.mobile.todo.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mobile.todo.database.dataset.Habit

@Dao
interface HabitDao {
    @Query("SELECT * FROM habit WHERE userId = :userId")
    fun getHabitsByUserId(userId: Int) : List<Habit>

    @Query("SELECT * FROM habit WHERE id = :id LIMIT 1;")
    fun getHabit(id: Int) : Habit

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertHabit(habit: Habit)

    @Query("UPDATE habit SET title = :title, description = :description WHERE id = :habitId")
    fun updateHabit(habitId: Int, title: String, description: String)

    @Delete
    fun deleteHabit(habit: Habit)
}