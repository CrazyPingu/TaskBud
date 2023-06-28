package com.mobile.todo.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatDelegate
import com.mobile.todo.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class Constant {

    companion object {

        val LOCKED_ICON : Uri = Uri.parse("android.resource://com.mobile.todo/drawable/badge_locked")

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

        fun logoutUser(context: Context){
            context.getSharedPreferences(
                context.resources.getString(R.string.shared_preferance_name),
                Context.MODE_PRIVATE
            ).edit().putInt("user_id", -1).apply()
        }

        fun getDefaultIcon(context: Context): Uri {
            return Uri.parse(
                ContentResolver.SCHEME_ANDROID_RESOURCE
                        + "://" + context.resources
                    .getResourcePackageName(R.drawable.default_profile_pic)
                        + '/' + context.resources.getResourceTypeName(R.drawable.default_profile_pic)
                        + '/' + context.resources.getResourceEntryName(R.drawable.default_profile_pic)
            )!!
        }

        fun getCurrentDate(): Date {
            val currentDate = Date()
            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formattedDate = formatter.format(currentDate)
            return formatter.parse(formattedDate)!!
        }

    }
}