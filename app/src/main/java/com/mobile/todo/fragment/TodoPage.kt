package com.mobile.todo.fragment

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.TypedValue
import android.util.Xml
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.mobile.todo.EditTodoHabit
import com.mobile.todo.R
import com.mobile.todo.adapter.SearchAdapter
import com.mobile.todo.adapter.ToDoAdapter
import com.mobile.todo.database.AppDatabase
import com.mobile.todo.database.dataset.Tag
import com.mobile.todo.database.dataset.ToDo
import com.mobile.todo.utils.Constant
import com.mobile.todo.utils.Monet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.google.android.material.search.SearchView
import com.google.android.material.search.SearchBar
import com.mobile.todo.HomePage
import kotlinx.coroutines.withContext
import org.xmlpull.v1.XmlPullParserFactory

class TodoPage : Fragment() {

    private var USER_ID: Int = 0
    private lateinit var searchBar: SearchBar
    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        if(!isAdded){
            return null
        }
        val view = inflater.inflate(R.layout.fragment_todo, container, false)
        val recyclerViewToDo = view.findViewById<RecyclerView>(R.id.recyclerViewToDo)
        val database = AppDatabase.getDatabase(requireContext())
        val itemList = mutableListOf<String>()

        val showAddButton = view.findViewById<ExtendedFloatingActionButton>(R.id.show_add)
        val addTodoButton = view.findViewById<ExtendedFloatingActionButton>(R.id.add_todo)
        val addHabitButton = view.findViewById<ExtendedFloatingActionButton>(R.id.add_habit)


        GlobalScope.launch(Dispatchers.IO) {
            val todo = database.toDoDao().getAllToDoByUserId(USER_ID)
            val search = database.searchDao().getAllSearch()
            val tagList = database.tagDao().getAllTag()
            for (tag in tagList) {
                if (database.searchDao().isTagInSearch(tag.tag) || tag.tag == Tag.FAV) itemList.add(
                    tag.tag
                )
            }

            if (todo.isEmpty()) {
                withContext(Dispatchers.Main) {
                    view.findViewById<TextView>(R.id.no_result).visibility = View.VISIBLE
                }
            }

            withContext(Dispatchers.Main) {
                recyclerViewToDo.adapter = ToDoAdapter(todo.toMutableList(), search.toMutableList())
                recyclerViewToDo.layoutManager = LinearLayoutManager(context)
            }
        }



        searchBar = view.findViewById(R.id.search_bar)
        searchView = view.findViewById(R.id.result_search)
        val suggestRecycler: RecyclerView = view.findViewById(R.id.suggest)

//        searchView.setupWithSearchBar(searchBar)

        if (Constant.getMonet(requireContext())) {
            Monet.setFabMonet(showAddButton, requireContext())
            Monet.setFabMonet(addTodoButton, requireContext())
            Monet.setFabMonet(addHabitButton, requireContext())
            Monet.setSearchBarMonet(searchBar, requireContext())

            // switch theme for the search view; check styles.xml
            searchView = Monet.setSearchViewMonet(searchView)
            searchView.setupWithSearchBar(searchBar)
        }


        suggestRecycler.adapter = SearchAdapter(itemList, searchView)

        suggestRecycler.layoutManager = LinearLayoutManager(requireContext())

        searchView.editText.doOnTextChanged { text, _, _, _ ->
            searchTag(text.toString(), database, suggestRecycler)
        }

        searchView.editText.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = v.text.toString()
                searchBar.text = searchView.text
                searchView.hide()
                performSearch(query, database, recyclerViewToDo, view.findViewById(R.id.no_result))
                true
            } else {
                false
            }
        }


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
                showAddButton.icon = ContextCompat.getDrawable(requireContext(), R.drawable.close_cross)
            } else {
                addTodoButton.startAnimation(fadeOutAnimation)
                addHabitButton.startAnimation(fadeOutAnimation)
                showAddButton.icon = ContextCompat.getDrawable(requireContext(), R.drawable.add_cross)
            }
        }

        addTodoButton.setOnClickListener {
            startActivity(
                Intent(
                    EditTodoHabit.newInstance(
                        requireContext(), EditTodoHabit.Companion.TYPE.TODO
                    )
                )
            )
        }

        addHabitButton.setOnClickListener {
            startActivity(
                Intent(
                    EditTodoHabit.newInstance(
                        requireContext(), EditTodoHabit.Companion.TYPE.HABIT
                    )
                )
            )
        }

        return view
    }

    private fun performSearch(
        query: String,
        database: AppDatabase,
        recyclerViewToDo: RecyclerView,
        no_result_text: TextView,
    ) {
        var resultToDoId: List<Int> // List of To Do Ids with submitted tag
        GlobalScope.launch(Dispatchers.IO) {
            val todo = database.toDoDao().getAllToDoByUserId(HomePage.USER_ID)
            if (query != null) {
                val filteredToDos: List<ToDo>
                val search = database.searchDao().getAllSearch()
                if (query == "") {
                    // case to show all todo
                    filteredToDos = todo
                } else {
                    // case to show todo with tag
                    resultToDoId = database.searchDao().getToDoIdsByTag(query)
                    filteredToDos = todo.filter { resultToDoId.contains(it.id) }
                }

                withContext(Dispatchers.Main) {
                    // Update the RecyclerView on the main thread
                    val adapter = ToDoAdapter(filteredToDos.toMutableList(), search.toMutableList())
                    recyclerViewToDo.adapter = adapter
                    recyclerViewToDo.layoutManager = LinearLayoutManager(requireContext())

                    if (filteredToDos.isEmpty()) {
                        no_result_text.visibility = View.VISIBLE
                    } else {
                        no_result_text.visibility = View.GONE
                    }
                }
            }
        }

        searchBar.clearFocus()
    }


    private fun searchTag(
        starting: String,
        database: AppDatabase,
        recyclerView: RecyclerView
    ) {
         GlobalScope.launch(Dispatchers.IO) {
            var tagList = if (starting.isNullOrEmpty()) {
                database.tagDao().getAllTagsFromUser(HomePage.USER_ID)
            } else {
                database.tagDao().getAllTagStartingWith(starting, HomePage.USER_ID)
            }
            tagList = tagList.toMutableList()

            tagList.remove(Tag.SHOW_ALL)
            tagList.add(0, Tag.SHOW_ALL)
            tagList.remove(Tag.FAV)
            tagList.add(1, Tag.FAV)

            withContext(Dispatchers.Main) {
                recyclerView.adapter = SearchAdapter(tagList, searchView)
            }
        }
    }

    companion object {
        fun newInstance(idUser: Int) = TodoPage().apply {
            arguments = Bundle().apply {
                USER_ID = idUser
            }
        }
    }
}


