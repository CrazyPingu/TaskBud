package com.mobile.todo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.mobile.todo.database.dataset.Tag
import com.mobile.todo.database.dataset.ToDo

@Dao
interface ToDoDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertTag(toDo: ToDo)

    @Query("SELECT * FROM todo where userId = :userId")
    fun getAllToDoByUserId(userId: Int): List<ToDo>

    @Query("SELECT * FROM todo where userId = :userId and tagId = :tagId")
    fun getAllToDoByTagId(userId: Int, tagId: Int): List<ToDo>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertToDo(todo: ToDo)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTag(tag: Tag)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertToDoWithTag(todo: ToDo)

    @Query("SELECT id FROM Tag WHERE name = :tagName AND userId = :userId")
    suspend fun getTagIdByName(tagName: String, userId: Int): Int?

    @Transaction
    suspend fun insertToDoWithTagCheck(todo: ToDo, tagName: String, userId: Int) {
        val existingTagId = getTagIdByName(tagName, userId)

        val tagId = if (existingTagId != null) {
            existingTagId
        } else {
            val newTag = Tag(name = tagName, userId = userId)
            insertTag(newTag)
            newTag.id
        }

        val todoWithTag = todo.copy(tagId = tagId)
        insertToDoWithTag(todoWithTag)
    }

    @Query("UPDATE todo SET title = :title, description = :description, date = :date, completed = :completed, tagId = :tagId WHERE id = :id")
    fun updateToDo(id: Int, title: String, description: String, date: String, completed: Boolean, tagId: Int)
}