package com.mobile.todo.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.mobile.todo.database.dataset.Tag
import com.mobile.todo.database.dataset.ToDo
import java.util.Date

@Dao
interface ToDoDao {
    @Query("SELECT * FROM todo where userId = :userId")
    fun getAllToDoByUserId(userId: Int): List<ToDo>

    @Query("SELECT * FROM todo where userId = :userId and tagId = :tagId")
    fun getAllToDoByTagId(userId: Int, tagId: Int): List<ToDo>

    @Query("SELECT * FROM todo WHERE id = :todoId")
    fun getToDoById(todoId: Int): ToDo?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertToDo(todo: ToDo)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTag(tag: Tag) : Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertToDoWithTag(todo: ToDo)

    @Query("SELECT id FROM Tag WHERE name = :tagName")
    suspend fun getTagIdByName(tagName: String): Int?

    @Transaction
    suspend fun insertToDoWithTagCheck(todo: ToDo, tagName: String) {
        val existingTagId = getTagIdByName(tagName)

        val tagId = if (existingTagId != null) {
            existingTagId
        } else {
            val insertedTagId = insertTag(Tag(tagName))
            insertedTagId.toInt()
        }

        val todoWithTag = todo.copy(tagId = tagId)
        insertToDoWithTag(todoWithTag)
    }

    @Query("UPDATE todo SET title = :title, description = :description, date = :date, tagId = :tagId WHERE id = :todoId")
    fun updateToDo(todoId: Int, title: String, description: String, date: Date?, tagId: Int)

    @Transaction
    suspend fun updateToDoWithTagCheck(todoId: Int, title: String, description: String, date: Date?, tagName: String) {
        val existingTagId = getTagIdByName(tagName)

        val tagId = if (existingTagId != null) {
            existingTagId
        } else {
            val insertedTagId = insertTag(Tag(tagName))
            insertedTagId.toInt()
        }

        updateToDo(todoId, title, description, date, tagId)
    }

    @Delete
    fun deleteToDo(todo: ToDo)

    @Query("UPDATE todo SET completed = :completed WHERE id = :todoId")
    fun setCompleted(todoId: Int, completed: Boolean)
}