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
import android.widget.TextView
import com.mobile.todo.R
import com.mobile.todo.database.AppDatabase
import com.mobile.todo.utils.Constant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.io.InputStream


class ProfilePage : Fragment() {

    private var USER_ID: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        GlobalScope.launch {
            val user = AppDatabase.getDatabase(requireContext()).userDao().getUser(USER_ID)
            var profilePicUri = user.profilePic
            try {
                // Search for profile pic
                val inputStream: InputStream? = context?.contentResolver?.openInputStream(profilePicUri)
                inputStream?.close()
            } catch (e: IOException) {
                // Case profile pic is not found
                AppDatabase.getDatabase(requireContext()).userDao().updateProfilePic(
                    USER_ID,
                    Constant.getDefaultIcon(requireContext()).toString()
                )
                profilePicUri = Constant.getDefaultIcon(requireContext())
            }

            // Switch to the main (UI) thread to update the ImageView
            launch(Dispatchers.Main) {
                view.findViewById<ImageView>(R.id.profile_pic).setImageURI(profilePicUri)
                view.findViewById<TextView>(R.id.username).text = user.username
                
            }
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