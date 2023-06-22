package com.mobile.todo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mobile.todo.database.converter.DateTypeConverter
import com.mobile.todo.database.converter.UriTypeConverter
import com.mobile.todo.database.dao.*
import com.mobile.todo.database.dataset.Badge
import com.mobile.todo.database.dataset.Tag
import com.mobile.todo.database.dataset.Habit
import com.mobile.todo.database.dataset.ToDo
import com.mobile.todo.database.dataset.User
import com.mobile.todo.database.dataset.UserBadge

@Database(
    entities = [User::class, Tag::class, ToDo::class, Habit::class, Badge::class, UserBadge::class],
    version = 1
)
@TypeConverters(DateTypeConverter::class, UriTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun tagDao(): TagDao
    abstract fun toDoDao(): ToDoDao
    abstract fun habitDao(): HabitDao
    abstract fun badgeDao(): BadgeDao
    abstract fun userBadgeDao(): UserBadgeDao

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