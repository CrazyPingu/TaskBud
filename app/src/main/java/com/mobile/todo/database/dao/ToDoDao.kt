package com.mobile.todo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mobile.todo.database.dataset.Habit
import com.mobile.todo.database.dataset.ToDo
import java.util.Date

@Dao
interface ToDoDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFolder(toDo: ToDo)

    @Query("SELECT * FROM todo where userId = :userId")
    fun getAllToDoByUserId(userId: Int): List<ToDo>

    @Query("SELECT * FROM todo where userId = :userId and folderId = :folderId")
    fun getAllToDoByTagId(userId: Int, folderId: Int): List<ToDo>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertToDo(todo: ToDo)

    @Query("UPDATE todo SET title = :title, description = :description, date = :date, completed = :completed, folderId = :folderId WHERE id = :id")
    fun updateToDo(id: Int, title: String, description: String, date: String, completed: Boolean, folderId: Int)
}