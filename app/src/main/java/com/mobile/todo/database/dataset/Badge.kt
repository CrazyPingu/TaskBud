package com.mobile.todo.database.dataset

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.lang.reflect.Modifier

@Entity
data class Badge(
    val name: String,
    val description: String,
    val streak_bp: Int?,
    val icon: Uri,
    @PrimaryKey val id: Int = 0
) {
    companion object {
        val allHabits: Badge = Badge(
            "All habits!",
            "Damn, you did all the daily habits?!?!",
            -1,
            Uri.parse("android.resource://com.mobile.todo/drawable/badge_all_habits"),
            1
        )

        val favourite: Badge = Badge(
            "Favourite!",
            "Are you a Sailor Moon fan?",
            -1,
            Uri.parse("android.resource://com.mobile.todo/drawable/badge_favtag"),
            2
        )

        val habitStreak: Badge = Badge(
            "Habit streak",
            "An habit done 3 times",
            3,
            Uri.parse("android.resource://com.mobile.todo/drawable/badge_habit_streak"),
            3
        )

        val todoStreak: Badge = Badge(
            "ToDo streak",
            "A todo done 3 times",
            3,
            Uri.parse("android.resource://com.mobile.todo/drawable/badge_todo_streak"),
            4
        )

        val niceShot: Badge = Badge(
            "Nice shot!",
            "Continue customize the profile!",
            null,
            Uri.parse("android.resource://com.mobile.todo/drawable/badge_pfp"),
            5
        )

        val tag: Badge = Badge(
            "Tag",
            "Tried tag!",
            1,
            Uri.parse("android.resource://com.mobile.todo/drawable/badge_tag"),
            6
        )

        val firstTodo: Badge = Badge(
            "First Todo",
            "Continue like this!",
            1,
            Uri.parse("android.resource://com.mobile.todo/drawable/badge_first_todo"),
            7
        )

        val trash: Badge = Badge(
            "Trash",
            "Deleted smth?",
            1,
            Uri.parse("android.resource://com.mobile.todo/drawable/badge_trash"),
            8
        )


        val allBadges: List<Badge> by lazy {
            val badgeFields = Badge::class.java.declaredFields
                .filter { field ->
                    field.type == Badge::class.java &&
                            Modifier.isStatic(field.modifiers) &&
                            Modifier.isFinal(field.modifiers)
                }

            badgeFields.map { field ->
                field.isAccessible = true
                field.get(null) as Badge
            }
        }
    }
}
