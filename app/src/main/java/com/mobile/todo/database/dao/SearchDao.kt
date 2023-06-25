package com.mobile.todo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mobile.todo.database.dataset.Search

@Dao
interface SearchDao {
    @Insert
    fun insertSearch(search: Search)

    @Query("SELECT * FROM Search")
    fun getAllSearch(): List<Search>

    // USE THIS FOR SEARCH TODO
    @Query("SELECT tag FROM Search WHERE toDoId = :toDoId")
    fun getTagsByToDoId(toDoId: Int): List<String>

    @Query("DELETE FROM Search WHERE toDoId = :toDoId AND tag = :tag")
    fun removeTagFromToDoId(toDoId: Int, tag: String)
}