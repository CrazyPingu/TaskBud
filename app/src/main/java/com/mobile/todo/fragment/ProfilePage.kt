package com.mobile.todo.fragment

import android.content.ContentResolver
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.mobile.todo.R
import com.mobile.todo.database.AppDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File


class ProfilePage : Fragment() {

    private var USER_ID: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Add all the code inside here

        GlobalScope.launch {
            val user = AppDatabase.getDatabase(requireContext()).userDao().getUser(USER_ID)
            val profilePicUri = user.profilePic

            if (!File(profilePicUri.path) .exists()) {
                // Handle case when the image file does not exist
                // Change the profile pic in the database to the default profile pic
                AppDatabase.getDatabase(requireContext()).userDao().updateProfilePic(
                    USER_ID,
                    Uri.parse(
                        ContentResolver.SCHEME_ANDROID_RESOURCE
                                + "://" + this@ProfilePage.resources
                            .getResourcePackageName(R.drawable.default_profile_pic)
                                + '/' + this@ProfilePage.resources.getResourceTypeName(R.drawable.default_profile_pic)
                                + '/' + this@ProfilePage.resources.getResourceEntryName(R.drawable.default_profile_pic)
                    )!!.toString()
                )
                view.findViewById<ImageView>(R.id.profile_pic)
                    .setImageResource(R.drawable.default_profile_pic)
            }

            view.findViewById<ImageView>(R.id.profile_pic).setImageURI(profilePicUri)
        }

        return view
    }


    companion object {
        fun newInstance(idUser: Int) =
            ProfilePage().apply {
                arguments = Bundle().apply {
                    USER_ID = idUser
                }
            }
    }
}