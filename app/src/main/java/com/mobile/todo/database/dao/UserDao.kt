package com.mobile.todo.database.dao

import androidx.room.*
import com.mobile.todo.database.dataset.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("SELECT * FROM user WHERE username = :username  and password = :password LIMIT 1;")
    fun getUser(username: String, password: String): User

    @Query("SELECT * FROM user WHERE username = :username LIMIT 1;")
    fun getUser(username: String): User

    @Query("SELECT * FROM user WHERE id = :userId LIMIT 1;")
    fun getUser(userId: Int): User

    @Query("UPDATE user SET profilePic = :profilePic WHERE id = :userId")
    fun updateProfilePic(userId: Int, profilePic: String)
}