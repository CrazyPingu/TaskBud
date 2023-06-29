package com.mobile.todo.utils

import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import androidx.core.content.ContextCompat.getSystemService
import com.mobile.todo.HomePage
import com.mobile.todo.R
import com.mobile.todo.Signup

class Shortcut {

    companion object {

        fun addSignup(context: Context) {
            val shortcut = ShortcutInfo.Builder(context, "sign_up")
                .setShortLabel("Sign Up")
                .setLongLabel("Sign Up")
                .setIcon(Icon.createWithResource(context, R.drawable.shortcut_signup_icon))
                .setIntent(Intent(Intent.ACTION_VIEW, null, context, Signup::class.java))
                .build()

            getSystemService(context, ShortcutManager::class.java)!!.dynamicShortcuts =
                listOf(shortcut)
        }

        fun removeSignup(context: Context) {
            getSystemService(
                context,
                ShortcutManager::class.java
            )!!.removeDynamicShortcuts(listOf("sign_up"))
        }


        fun addTodoHabit(context: Context) {
            val todoShortcut = ShortcutInfo.Builder(context, "todo")
                .setShortLabel("Todo")
                .setLongLabel("Todo")
                .setIcon(Icon.createWithResource(context, R.drawable.shortcut_todo_icon))
                .setIntent(Intent(Intent.ACTION_VIEW, null, context, HomePage::class.java).apply {
                    putExtra("page", R.id.navbar_todo)
                })
                .build()

            val habitShortcut = ShortcutInfo.Builder(context, "habit")
                .setShortLabel("Habit")
                .setLongLabel("Habit")
                .setIcon(Icon.createWithResource(context, R.drawable.shortcut_habit_icon))
                .setIntent(Intent(Intent.ACTION_VIEW, null, context, HomePage::class.java).apply {
                    putExtra("page", R.id.navbar_habit)
                })
                .build()

            getSystemService(context, ShortcutManager::class.java)!!.dynamicShortcuts =
                listOf(todoShortcut, habitShortcut)
        }

        fun removeTodoHabit(context: Context) {
            getSystemService(
                context,
                ShortcutManager::class.java
            )!!.removeDynamicShortcuts(listOf())
        }
    }
}