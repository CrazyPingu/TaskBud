package com.mobile.todo.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobile.todo.EditTodoHabit
import com.mobile.todo.R
import com.mobile.todo.adapter.HabitAdapter
import com.mobile.todo.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HabitPage : Fragment() {

    private var userId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = arguments?.getInt(ARG_USER_ID) ?: 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_habit, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)

        val database = AppDatabase.getDatabase(requireContext())

        GlobalScope.launch {
            val habit = database.habitDao().getHabitsByUserId(userId)
            withContext(Dispatchers.Main) {
                recyclerView.adapter = HabitAdapter(habit.toMutableList())
                recyclerView.layoutManager = LinearLayoutManager(context)
                if (habit.isEmpty()) {
                    view.findViewById<View>(R.id.no_result).visibility = View.VISIBLE
                } else {
                    view.findViewById<View>(R.id.no_result).visibility = View.GONE
                }
            }
        }

        view.findViewById<ImageView>(R.id.add_todo).setOnClickListener {
            startActivity(
                Intent(
                    EditTodoHabit.newInstance(
                        requireContext(),
                        EditTodoHabit.Companion.TYPE.TODO
                    )
                )
            )
        }

        view.findViewById<ImageView>(R.id.add_habit).setOnClickListener {
            startActivity(
                Intent(
                    EditTodoHabit.newInstance(
                        requireContext(),
                        EditTodoHabit.Companion.TYPE.HABIT
                    )
                )
            )
        }

        return view
    }

    companion object {
        private const val ARG_USER_ID = "user_id"

        fun newInstance(userId: Int): HabitPage {
            val fragment = HabitPage()
            val args = Bundle()
            args.putInt(ARG_USER_ID, userId)
            fragment.arguments = args
            return fragment
        }
    }
}
