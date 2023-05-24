package com.mobile.todo.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import com.mobile.todo.R

class Constant {

    companion object {

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