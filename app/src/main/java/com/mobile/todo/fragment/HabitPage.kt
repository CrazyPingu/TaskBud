package com.mobile.todo.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mobile.todo.R


class HabitPage : Fragment() {

    private var USER_ID: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_habit, container, false)

        // Add all the code inside here

        return view
    }


    companion object {
        fun newInstance(idUser: Int) =
            HabitPage().apply {
                arguments = Bundle().apply {
                    USER_ID = idUser
                }
            }
    }
}