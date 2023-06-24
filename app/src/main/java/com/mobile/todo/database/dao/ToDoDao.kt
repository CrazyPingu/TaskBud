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

    // Get the last inserted ID in the ToDo table
    @Query("SELECT MAX(id) FROM ToDo")
    suspend fun getLastInsertedId(): Int?

    //@Query("UPDATE todo SET title = :title, description = :description, date = :date, tags = :tags WHERE id = :todoId")
    //fun updateToDo(todoId: Int, title: String, description: String, date: Date?, tags: List<String>)

    /*@Transaction
    suspend fun updateToDoWithTagCheck(todoId: Int, title: String, description: String, date: Date?, tagNames: List<String>) {
        val existingTagIds = getTagIdsByName(tagNames)
        val newTagNames = tagNames.filterIndexed { index, _ -> existingTagIds[index] == null }

        val newTagIds = newTagNames.map { tagName ->
            val insertedTagId = insertTag(Tag(tagName))
            insertedTagId.toString()
        }

        val tagIds = existingTagIds.mapNotNull { it } + newTagIds

        updateToDo(todoId, title, description, date, tagIds)
    }*/

    @Delete
    fun deleteToDo(todo: ToDo)

    @Query("UPDATE todo SET completed = :completed WHERE id = :todoId")
    fun setCompleted(todoId: Int, completed: Boolean)
}
