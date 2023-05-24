package com.mobile.todo.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.mobile.todo.R
import com.mobile.todo.database.AppDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


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
            view.findViewById<ImageView>(R.id.profile_pic).setImageURI(user.profilePic)
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