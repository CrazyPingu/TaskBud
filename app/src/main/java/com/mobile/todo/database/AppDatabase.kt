package com.mobile.todo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mobile.todo.database.dao.UserDao
import com.mobile.todo.database.dataset.Badge
import com.mobile.todo.database.dataset.Folder
import com.mobile.todo.database.dataset.Habit
import com.mobile.todo.database.dataset.ToDo
import com.mobile.todo.database.dataset.User
import com.mobile.todo.database.dataset.UserBadge

@Database(
    entities = [User::class, Folder::class, ToDo::class, Habit::class, Badge::class, UserBadge::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {

        @Volatile
        private var INSTANCE : AppDatabase? = null

        fun getDatabase(context : Context) : AppDatabase {

            val tempInstance = INSTANCE
            if(tempInstance != null) {

                return tempInstance

            }
            synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "user_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}