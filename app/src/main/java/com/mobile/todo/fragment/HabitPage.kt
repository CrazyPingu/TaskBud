package com.mobile.todo.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.mobile.todo.EditTodoHabit
import com.mobile.todo.R
import com.mobile.todo.adapter.HabitAdapter
import com.mobile.todo.database.AppDatabase
import com.mobile.todo.utils.Constant
import com.mobile.todo.utils.Monet
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
        if(!isAdded){
            return null
        }
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_habit, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)

        val database = AppDatabase.getDatabase(requireContext())

        val showAddButton = view.findViewById<ExtendedFloatingActionButton>(R.id.show_add)
        val addTodoButton = view.findViewById<ExtendedFloatingActionButton>(R.id.add_todo)
        val addHabitButton = view.findViewById<ExtendedFloatingActionButton>(R.id.add_habit)

        if(Constant.getMonet(requireContext())){
            Monet.setFabMonet(showAddButton, requireContext())
            Monet.setFabMonet(addTodoButton, requireContext())
            Monet.setFabMonet(addHabitButton, requireContext())
        }


        GlobalScope.launch(Dispatchers.IO) {
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



        // FAB
        val fadeInAnimation = AnimationUtils.loadAnimation(context, R.anim.fab_menu_shown)
        val fadeOutAnimation = AnimationUtils.loadAnimation(context, R.anim.fab_menu_hide)

        fadeInAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                showAddButton.isClickable = false
            }

            override fun onAnimationEnd(animation: Animation) {
                showAddButton.isClickable = true
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })

        fadeOutAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                showAddButton.isClickable = false
            }

            override fun onAnimationEnd(animation: Animation) {
                addTodoButton.visibility = View.GONE
                addHabitButton.visibility = View.GONE
                showAddButton.isClickable = true
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })

        showAddButton.setOnClickListener {
            if (addTodoButton.visibility == View.GONE) {
                addTodoButton.visibility = View.VISIBLE
                addHabitButton.visibility = View.VISIBLE
                addTodoButton.startAnimation(fadeInAnimation)
                addHabitButton.startAnimation(fadeInAnimation)
            } else {
                addTodoButton.startAnimation(fadeOutAnimation)
                addHabitButton.startAnimation(fadeOutAnimation)
            }
        }

        addTodoButton.setOnClickListener {
            startActivity(
                Intent(
                    EditTodoHabit.newInstance(
                        requireContext(),
                        EditTodoHabit.Companion.TYPE.TODO
                    )
                )
            )
        }

        addHabitButton.setOnClickListener {
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
