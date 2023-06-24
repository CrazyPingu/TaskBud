package com.mobile.todo.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobile.todo.EditTodoHabit
import com.mobile.todo.R
import com.mobile.todo.adapter.TodoAdapter
import com.mobile.todo.database.AppDatabase
import com.mobile.todo.database.dataset.ToDo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TodoPage : Fragment() {

    private var USER_ID: Int = 0
    private lateinit var searchView: SearchView
    private lateinit var arrayAdapter: ArrayAdapter<String>
    private lateinit var handler: Handler


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_todo, container, false)

        val recyclerViewToDo = view.findViewById<RecyclerView>(R.id.recyclerViewToDo)

        val database = AppDatabase.getDatabase(requireContext())

        GlobalScope.launch {
            val todo = database.toDoDao().getAllToDoByUserId(USER_ID)

            withContext(Dispatchers.Main) {
                recyclerViewToDo.adapter = TodoAdapter(todo.toMutableList())
                recyclerViewToDo.layoutManager = LinearLayoutManager(context)
            }
        }

        searchView = view.findViewById(R.id.search_view)
        val listView = view.findViewById<ListView>(R.id.list_search_view)

        val itemList = listOf("a", "b", "c", "d", "e", "f", "g", "h", "i", "j")

        arrayAdapter = ArrayAdapter(view.context, android.R.layout.simple_list_item_1, itemList)

        listView.setOnItemClickListener { _, _, position, _ ->
            val item = arrayAdapter.getItem(position) as String
        }
        listView.adapter = arrayAdapter

        handler = Handler(Looper.getMainLooper())

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // TODO Handle query submit if needed
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                handler.post {
                    // TODO Execute query when text changes
                    arrayAdapter.filter.filter(newText)
                }
                return true
            }
        })

        searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                listView.visibility = View.VISIBLE
                recyclerViewToDo.visibility = View.GONE
            } else {
                listView.visibility = View.GONE
                recyclerViewToDo.visibility = View.VISIBLE
            }
        }

        view.findViewById<ImageView>(R.id.add_todo).setOnClickListener {
            startActivity(Intent(EditTodoHabit.newInstance(requireContext(), EditTodoHabit.Companion.TYPE.TODO)))
        }


        view.findViewById<ImageView>(R.id.add_habit).setOnClickListener {
            startActivity(Intent(EditTodoHabit.newInstance(requireContext(), EditTodoHabit.Companion.TYPE.HABIT)))
        }

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
