package com.mobile.todo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mobile.todo.database.dataset.Habit
import com.mobile.todo.database.dataset.User

@Dao
interface HabitDao {
    @Query("SELECT * FROM habit WHERE userId = :userId")
    fun getHabits(userId: Int) : List<Habit>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertHabit(habit: Habit)

    @Query("UPDATE habit SET title = :title and description = :description WHERE id = :habitId")
    fun updateHabit(habitId: Int, title: String, description : String)
}