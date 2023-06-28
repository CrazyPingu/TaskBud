package com.mobile.todo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.mobile.todo.database.converter.DateTypeConverter
import com.mobile.todo.database.converter.UriTypeConverter
import com.mobile.todo.database.dao.*
import com.mobile.todo.database.dataset.Badge
import com.mobile.todo.database.dataset.Tag
import com.mobile.todo.database.dataset.Habit
import com.mobile.todo.database.dataset.Search
import com.mobile.todo.database.dataset.ToDo
import com.mobile.todo.database.dataset.User
import com.mobile.todo.database.dataset.UserBadge

@Database(
    entities = [User::class, Tag::class, ToDo::class, Habit::class, Badge::class, UserBadge::class, Search::class],
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
    abstract fun searchDao(): SearchDao

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
                ).addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Insert the initial row with "favorites" tag
                        db.execSQL("INSERT INTO Tag (tag) VALUES ('favorites')")

                        // Insert the badge
                        // o todo 1 habit
                        db.execSQL("INSERT INTO Badge(name, description, streak_bp, type, icon) VALUES (" +
                                "'All habits', 'Damn, you did all the habits?!?! ',-1,1," +
                                "'android.resource://com.mobile.todo/drawable/badge_all_habits')" +
                                "")

                        db.execSQL("INSERT INTO Badge(name, description, streak_bp, type, icon) VALUES (" +
                                "'Favourite!', 'Are you a sailor moon fan?',-1,1," +
                                "'android.resource://com.mobile.todo/drawable/badge_favtag')" +
                                "")

                        db.execSQL("INSERT INTO Badge(name, description, streak_bp, type, icon) VALUES (" +
                                "'Habit streak', 'An habit done 3 times',3,1," +
                                "'android.resource://com.mobile.todo/drawable/badge_habit_streak')" +
                                "")

                        db.execSQL("INSERT INTO Badge(name, description, streak_bp, type, icon) VALUES (" +
                                "'ToDo streak', 'A todo done 3 times',3,0," +
                                "'android.resource://com.mobile.todo/drawable/badge_todo_streak')" +
                                "")

                        db.execSQL("INSERT INTO Badge(name, description, streak_bp, type, icon) VALUES (" +
                                "'Nice shot!', 'Continue customize the profile!',-1,1," +
                                "'android.resource://com.mobile.todo/drawable/badge_pfp')" +
                                "")

                        db.execSQL("INSERT INTO Badge(name, description, streak_bp, type, icon) VALUES (" +
                                "'Tag', 'Tried tag!',-1,1," +
                                "'android.resource://com.mobile.todo/drawable/badge_tag')" +
                                "")

                        db.execSQL("INSERT INTO Badge(name, description, streak_bp, type, icon) VALUES (" +
                                "'First Todo', 'Continue like this!',1,0," +
                                "'android.resource://com.mobile.todo/drawable/badge_first_todo')" +
                                "")

                        db.execSQL("INSERT INTO Badge(name, description, streak_bp, type, icon) VALUES (" +
                                "'Trash', 'Deleted smth?',1,0," +
                                "'android.resource://com.mobile.todo/drawable/badge_trash')" +
                                "")

                    }
                }).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}