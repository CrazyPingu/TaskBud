package com.mobile.todo.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobile.todo.HomePage
import com.mobile.todo.R
import com.mobile.todo.adapter.CustomAdapter
import com.mobile.todo.database.AppDatabase
import com.mobile.todo.database.dataset.Habit
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class HabitPage : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_habit, container, false)

        // Add all the code inside here
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)

        val database = AppDatabase.getDatabase(requireContext())

        GlobalScope.launch {
            val habit = database.habitDao().getHabits(HomePage.USER_ID)

            // Remove comment to test
//            val habit = listOf(
//                Habit(1, "Habit 1", "Description 1", 1, 1),
//                Habit(2, "Habit 2", "Description 2", 1, 1),
//                Habit(3, "Habit 3", "Description 3", 1, 1),
//            )
            recyclerView.adapter = CustomAdapter(habit)
            recyclerView.layoutManager = LinearLayoutManager(context)
        }
        return view
    }
}