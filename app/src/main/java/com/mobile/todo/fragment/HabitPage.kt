package com.mobile.todo.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobile.todo.EditTodoHabit
import com.mobile.todo.HomePage
import com.mobile.todo.R
import com.mobile.todo.adapter.HabitAdapter
import com.mobile.todo.database.AppDatabase
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
            val habit = database.habitDao().getHabitsByUserId(HomePage.USER_ID)
            recyclerView.adapter = HabitAdapter(habit.toMutableList())
            recyclerView.layoutManager = LinearLayoutManager(context)
        }

        view.findViewById<ImageView>(R.id.add_habit).setOnClickListener {
            startActivity(Intent(EditTodoHabit.newInstance(requireContext(), EditTodoHabit.Companion.TYPE.HABIT)))
        }

        return view
    }
}