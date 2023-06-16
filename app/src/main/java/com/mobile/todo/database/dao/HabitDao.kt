package com.mobile.todo.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mobile.todo.database.dataset.Habit
import java.util.Date

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

    @Query("UPDATE habit SET streak = streak + 1, lastDayCompleted = :currentDate WHERE id = :habitId")
    fun increaseStreak(habitId: Int, currentDate: Date) : Void

    @Query("UPDATE habit SET streak = streak - 1, lastDayCompleted = null WHERE id = :habitId")
    fun resetLastDay(habitId: Int)

    @Query("SELECT COUNT(*) FROM habit WHERE lastDayCompleted IS NOT NULL AND userId = :userId")
    fun getCompletedHabitsCount(userId: Int): Int

    @Query("SELECT COUNT(*) FROM habit WHERE userId = :userId")
    fun getUserHabitsCount(userId: Int): Int

    @Delete
    fun deleteHabit(habit: Habit)
}