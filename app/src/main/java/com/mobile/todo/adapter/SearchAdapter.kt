package com.mobile.todo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.search.SearchView
import com.mobile.todo.R


class SearchAdapter(
    private var itemList: MutableList<String>,
    private var searchView: SearchView,
) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    private lateinit var context: Context
    private val searchViewText: EditText = searchView.editText

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context // Store the reference to the parent ViewGroup
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_search, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]

        holder.textView.text = item

        holder.textView.setOnClickListener(
            View.OnClickListener {
                searchViewText.setText(item)
                searchViewText.setSelection(searchViewText.text.length)


                val editorInfo = EditorInfo()
                editorInfo.actionId = EditorInfo.IME_ACTION_SEARCH

                // Simulate the press of the search button
                searchViewText.onEditorAction(editorInfo.actionId)
            }
        )
    }


    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textView)
    }
}
