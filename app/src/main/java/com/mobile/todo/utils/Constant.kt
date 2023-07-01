package com.mobile.todo.utils

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Build
import android.widget.EditText
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.mobile.todo.R
import com.mobile.todo.fragment.SettingsPage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Constant {

    companion object {

        val LOCKED_ICON: Uri = Uri.parse("android.resource://com.mobile.todo/drawable/badge_locked")
        val DEFAULT_PROFILE_PIC: Uri =
            Uri.parse("android.resource://com.mobile.todo/drawable/default_profile_pic")

        fun getTheme(context: Context): Int {
            return context.getSharedPreferences(
                context.resources.getString(R.string.shared_preferance_name),
                Context.MODE_PRIVATE
            ).getInt("theme", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }


        fun saveTheme(context: Context, theme: Int) {
            context.getSharedPreferences(
                context.resources.getString(R.string.shared_preferance_name),
                Context.MODE_PRIVATE
            ).edit().putInt("theme", theme).apply()

            setTheme(context)
        }

        fun setTheme(context: Context) {
            AppCompatDelegate.setDefaultNightMode(getTheme(context))
        }

        fun saveMonet(context: Context, monetStatus: Boolean) {
            context.getSharedPreferences(
                context.resources.getString(R.string.shared_preferance_name),
                Context.MODE_PRIVATE
            ).edit().putBoolean("monet", monetStatus).apply()
        }

        fun checkVersionMonet(): Boolean {
            return Build.VERSION.SDK_INT >= 31
        }

        fun getMonet(context: Context): Boolean {
            if (!checkVersionMonet()) {
                return false
            }
            return context.getSharedPreferences(
                context.resources.getString(R.string.shared_preferance_name),
                Context.MODE_PRIVATE
            ).getBoolean("monet", false)
        }

        fun saveUser(context: Context, id: Int) {
            context.getSharedPreferences(
                context.resources.getString(R.string.shared_preferance_name),
                Context.MODE_PRIVATE
            ).edit().putInt("user_id", id).apply()
        }

        fun getUser(context: Context): Int {
            return context.getSharedPreferences(
                context.resources.getString(R.string.shared_preferance_name),
                Context.MODE_PRIVATE
            ).getInt("user_id", -1)
        }

        fun logoutUser(context: Context) {
            context.getSharedPreferences(
                context.resources.getString(R.string.shared_preferance_name),
                Context.MODE_PRIVATE
            ).edit().putInt("user_id", -1).apply()
        }

        fun getCurrentDate(): Date {
            val currentDate = Date()
            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formattedDate = formatter.format(currentDate)
            return formatter.parse(formattedDate)!!
        }

    }
}