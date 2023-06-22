package com.mobile.todo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.mobile.todo.database.dataset.Search

@Dao
interface SearchDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertSearch(search: Search)
}