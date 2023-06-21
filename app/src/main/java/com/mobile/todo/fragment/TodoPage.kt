package com.mobile.todo.fragment
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.mobile.todo.R


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


        searchView = view.findViewById(R.id.search_view)
        val listView = view.findViewById<ListView>(R.id.list_search_view)

        val itemList = listOf("a", "b", "c", "d", "e", "f", "g", "h", "i", "j")

        arrayAdapter = ArrayAdapter(view.context, android.R.layout.simple_list_item_1, itemList)

        listView.setOnItemClickListener { _, _, position, _ ->
            val item = arrayAdapter.getItem(position) as String
            Log.d("ListClick", item)
        }
        listView.adapter = arrayAdapter

        handler = Handler(Looper.getMainLooper())

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle query submit if needed
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                handler.post {
                    // Execute query when text changes
                    arrayAdapter.filter.filter(newText)
                }
                return true
            }
        })

        searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                listView.visibility = View.VISIBLE
            } else {
                listView.visibility = View.GONE
            }
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
