package com.mobile.todo.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.mobile.todo.database.dataset.Badge
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
    fun getLastInsertedId(): Int


    @Query("UPDATE todo SET title = :title, description = :description, date = :date WHERE id = :todoId")
    fun updateToDo(todoId: Int, title: String, description: String, date: Date?)

    @Delete
    fun deleteToDo(todo: ToDo)

    @Query("UPDATE todo SET completed = :completed WHERE id = :todoId")
    fun setCompleted(todoId: Int, completed: Boolean)

    @Query("SELECT COUNT(*) AS num FROM ToDo WHERE completed = 1 and userId = :userId")
    fun getNumCompleted(userId: Int) : Int

    @Query("SELECT COUNT(*) >= :streakPb FROM ToDo WHERE completed = 1 AND userId = :userId")
    fun badgeTodoStreak(userId: Int, streakPb : Int = Badge.todoStreak.streak_bp!!): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM ToDo WHERE userId = :userId)")
    fun hasToDo(userId: Int): Boolean

    @Query("SELECT todo.id FROM todo, search, tag WHERE todo.id = search.todoId AND search.tag = tag.tag AND tag.tag = :tag AND todo.userId = :userId")
    fun getToDoByUserIdTag(userId: Int, tag: String = Tag.FAV): List<Int>

}
