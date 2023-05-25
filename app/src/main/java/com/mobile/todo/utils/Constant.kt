package com.mobile.todo.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatDelegate
import com.mobile.todo.R

class Constant {

    companion object {

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

        fun getDefaultIcon(context: Context): Uri {
            return Uri.parse(
                ContentResolver.SCHEME_ANDROID_RESOURCE
                        + "://" + context.resources
                    .getResourcePackageName(R.drawable.default_profile_pic)
                        + '/' + context.resources.getResourceTypeName(R.drawable.default_profile_pic)
                        + '/' + context.resources.getResourceEntryName(R.drawable.default_profile_pic)
            )!!
        }
    }
}