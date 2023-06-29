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
            "Full pie!",
            "No habits left for today!",
            -1,
            Uri.parse("android.resource://com.mobile.todo/drawable/badge_all_habits"),
        )

        val favourite: Badge = Badge(
            "Favourite!",
            "Starred a to do, bright idea!",
            -1,
            Uri.parse("android.resource://com.mobile.todo/drawable/badge_favtag"),
        )

        val habitStreak: Badge = Badge(
            "Persistent",
            "3 days of habit streak!",
            3,
            Uri.parse("android.resource://com.mobile.todo/drawable/badge_habit_streak"),
        )

        val todoStreak: Badge = Badge(
            "Clear space",
            "3 to do completed, awesome!",
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
            "Organizer",
            "So tidy you have 3 tags!",
            3,
            Uri.parse("android.resource://com.mobile.todo/drawable/badge_tag"),
        )

        val firstTodo: Badge = Badge(
            "First step",
            "First to do created, keep going!",
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
