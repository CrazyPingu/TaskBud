package com.mobile.todo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mobile.todo.database.dataset.Tag

@Dao
interface TagDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertTag(tag: Tag)

    @Query("SELECT * FROM tag")
    fun getAllTag(): List<Tag>

    @Query("SELECT EXISTS(SELECT 1 FROM Search WHERE tag = :tag AND toDoId IN (SELECT id FROM ToDo WHERE userId = :userId))")
    fun usedFavouriteTag(userId: Int, tag: String = Tag.FAV): Boolean

    @Query("SELECT COUNT(DISTINCT tag) >= 3 FROM Search WHERE tag != :favoriteTag AND toDoId IN (SELECT id FROM ToDo WHERE userId = :userId)")
    fun usedTagBadge(userId: Int, favoriteTag: String = Tag.FAV): Boolean

    @Query("SELECT tag.tag " +
            "FROM tag " +
            "JOIN search ON tag.tag = search.tag " +
            "JOIN todo ON search.todoId = todo.id " +
            "WHERE tag.tag LIKE :starting || '%' AND todo.userId = :userId " +
            "GROUP BY tag.tag " +
            "ORDER BY COUNT(tag.tag) DESC " +
            "LIMIT 5")
    fun getAllTagStartingWith(starting: String, userId: Int): List<String>

    @Query("SELECT DISTINCT tag.tag " +
            "FROM tag " +
            "JOIN search ON tag.tag = search.tag " +
            "JOIN todo ON search.todoId = todo.id " +
            "WHERE todo.userId = :userId")
    fun getAllTagsFromUser(userId: Int): List<String>



}