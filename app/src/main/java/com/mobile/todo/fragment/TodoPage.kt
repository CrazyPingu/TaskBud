package com.mobile.todo.fragment
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.mobile.todo.R
import com.mobile.todo.database.AppDatabase


class TodoPage : Fragment() {

    private var USER_ID: Int = 0
    private lateinit var arrayAdapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_todo, container, false)

        val searchView = view.findViewById<SearchView>(R.id.search_view)
        val listView = view.findViewById<ListView>(R.id.list_search_view)

        val yourDao = AppDatabase.getDatabase(view.context).folderDao()

        val itemList = listOf("a", "b", "c", "d", "e", "f", "g", "h", "i", "j")

        arrayAdapter = ArrayAdapter<String>(view.context, android.R.layout.simple_list_item_1, itemList)

        listView.setOnItemClickListener { parent, view, position, id ->
            val item = parent.getItemAtPosition(position) as String
            if(item.isNotEmpty()) {
                Log.d("search", item)
            }
        }
        listView.adapter = arrayAdapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // you can handle query submit here if you want
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // here you can execute a query every time a character is written
                Log.d("search", newText.toString())
                arrayAdapter.filter.filter(newText)
                return true
            }
        })

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
