package com.mobile.todo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.mobile.todo.database.dataset.Search

@Dao
interface SearchDao {
    @Insert
    fun insertSearch(search: Search)

    @Query("SELECT * FROM Search")
    fun getAllSearch(): List<Search>

    @Query("SELECT EXISTS(SELECT 1 FROM Search WHERE tag = :tag LIMIT 1)")
    fun isTagInSearch(tag: String): Boolean

    @Query("SELECT todoId FROM Search WHERE tag = :tag")
    fun getToDoIdsByTag(tag: String): List<Int>

    // USE THIS FOR SEARCH TODO
    @Query("SELECT tag FROM Search WHERE toDoId = :toDoId")
    fun getTagsByToDoId(toDoId: Int): List<String>

    @Query("DELETE FROM Search WHERE toDoId = :toDoId AND tag = :tag")
    fun removeTagFromToDoId(toDoId: Int, tag: String)

    @Query("INSERT OR IGNORE INTO Search (toDoId, tag) VALUES (:toDoId, :tag)")
    fun addTagToToDoId(toDoId: Int, tag: String)

    @Query("DELETE FROM Search WHERE toDoId = :toDoId")
    fun removeByToDoId(toDoId: Int)

    @Transaction
    fun updateTagsForToDoId(toDoId: Int, tags: List<String>) {
        // Remove all existing tags for the toDoId
        removeByToDoId(toDoId)

        // Add new tags for the toDoId
        for (tag in tags) {
            addTagToToDoId(toDoId, tag)
        }
    }
}