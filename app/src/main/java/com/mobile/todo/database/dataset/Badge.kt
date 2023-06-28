package com.mobile.todo.database.dataset

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.lang.reflect.Modifier

@Entity
data class Badge(
    @PrimaryKey val name: String,
    val description: String,
    val streak_bp: Int?,
    val icon: Uri,
) {
    companion object {
        val allHabits: Badge = Badge(
            "All habits!",
            "Damn, you did all the daily habits?!?!",
            -1,
            Uri.parse("android.resource://com.mobile.todo/drawable/badge_all_habits"),
        )

        val favourite: Badge = Badge(
            "Favourite!",
            "Are you a Sailor Moon fan?",
            -1,
            Uri.parse("android.resource://com.mobile.todo/drawable/badge_favtag"),
        )

        val habitStreak: Badge = Badge(
            "Habit streak",
            "An habit done 3 times",
            3,
            Uri.parse("android.resource://com.mobile.todo/drawable/badge_habit_streak"),
        )

        val todoStreak: Badge = Badge(
            "To Do streak",
            "A to do done 3 times",
            3,
            Uri.parse("android.resource://com.mobile.todo/drawable/badge_todo_streak"),
        )

        val niceShot: Badge = Badge(
            "Nice shot!",
            "Continue customize the profile!",
            null,
            Uri.parse("android.resource://com.mobile.todo/drawable/badge_pfp"),
        )

        val tag: Badge = Badge(
            "Tag",
            "Used tag 3 times!?!",
            3,
            Uri.parse("android.resource://com.mobile.todo/drawable/badge_tag"),
        )

        val firstTodo: Badge = Badge(
            "First To Do",
            "Continue like this!",
            1,
            Uri.parse("android.resource://com.mobile.todo/drawable/badge_first_todo"),
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
