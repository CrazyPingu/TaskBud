package com.mobile.todo.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mobile.todo.R


class TodoPage : Fragment() {

    private var USER_ID: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_todo, container, false)

        // Add all the code inside here EXAMPLE BELOW
// Find the button by ID
//        myButton = view.findViewById(R.id.myButton)
//
//        // Set a click listener on the button
//        myButton.setOnClickListener {
//            // Handle button click event
//            // Put your desired logic here
//            Toast.makeText(activity, "Button Clicked!", Toast.LENGTH_SHORT).show()
//        }

        return view
    }


    companion object {
        fun newInstance(idUser: Int) =
            TodoPage().apply {
                arguments = Bundle().apply {
                    USER_ID = idUser
                }
            }
    }
}