package com.mobile.todo.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.mobile.todo.database.dataset.Search
import com.mobile.todo.database.dataset.Tag
import com.mobile.todo.database.dataset.ToDo
import java.util.Date

@Dao
interface ToDoDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertToDo(todo: ToDo)

    @Query("SELECT * FROM todo WHERE userId = :userId")
    fun getAllToDoByUserId(userId: Int): List<ToDo>

    @Query("SELECT * FROM todo WHERE id = :todoId")
    fun getToDoById(todoId: Int): ToDo?

    // Get the last inserted ID in the To Do table
    @Query("SELECT MAX(id) FROM ToDo")
    suspend fun getLastInsertedId(): Int?

    @Query("UPDATE todo SET title = :title, description = :description, date = :date WHERE id = :todoId")
    fun updateToDo(todoId: Int, title: String, description: String, date: Date?)

    @Delete
    fun deleteToDo(todo: ToDo)

    @Query("UPDATE todo SET completed = :completed WHERE id = :todoId")
    fun setCompleted(todoId: Int, completed: Boolean)
}
