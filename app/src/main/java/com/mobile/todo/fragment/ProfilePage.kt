package com.mobile.todo.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.mobile.todo.Camera
import com.mobile.todo.HomePage
import com.mobile.todo.R
import com.mobile.todo.database.AppDatabase
import com.mobile.todo.utils.Constant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStream


class ProfilePage : Fragment() {

    private var PROFILE_PIC_IMAGE: Uri = Uri.EMPTY

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)


        GlobalScope.launch {
            val user = AppDatabase.getDatabase(requireContext()).userDao().getUser(HomePage.USER_ID)
            var profilePicUri = user.profilePic
            if (PROFILE_PIC_IMAGE != Uri.EMPTY) {
                AppDatabase.getDatabase(requireContext()).userDao().updateProfilePic(
                    HomePage.USER_ID,
                    PROFILE_PIC_IMAGE.toString()
                )
                profilePicUri = PROFILE_PIC_IMAGE
            } else {
                // obtain the image URI
                try {
                    // Search for profile pic
                    val inputStream: InputStream? =
                        context?.contentResolver?.openInputStream(profilePicUri)
                    inputStream?.close()
                } catch (e: IOException) {
                    // Case profile pic is not found
                    AppDatabase.getDatabase(requireContext()).userDao().updateProfilePic(
                        HomePage.USER_ID,
                        Constant.getDefaultIcon(requireContext()).toString()
                    )
                    profilePicUri = Constant.getDefaultIcon(requireContext())
                }
            }

            // Switch to the main (UI) thread to update the ImageView
            launch(Dispatchers.Main) {
                view.findViewById<ImageView>(R.id.profile_pic).setImageURI(profilePicUri)
                view.findViewById<TextView>(R.id.username).text = user.username

                view.findViewById<ImageView>(R.id.profile_pic).setOnClickListener {
                    val intent = Intent(context, Camera::class.java)
                    intent.putExtra("profilePic", profilePicUri)
                    Camera.PAGE_TO_RETURN = HomePage::class
                    startActivity(intent)
                }
            }
        }
        return view
    }


    companion object {
        fun newInstance(profilePicImage: Uri = Uri.EMPTY) =
            ProfilePage().apply {
                arguments = Bundle().apply {
                    PROFILE_PIC_IMAGE = profilePicImage
                }
            }
    }
}