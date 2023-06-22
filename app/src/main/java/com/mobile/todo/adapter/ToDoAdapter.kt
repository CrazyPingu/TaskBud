package com.mobile.todo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobile.todo.R
import java.util.*
import kotlin.collections.ArrayList

class TodoAdapter(
    private val context: Context,
    private val itemList: ArrayList<String>
) : RecyclerView.Adapter<TodoAdapter.ViewHolder>(), Filterable {

    private val originalItemList: ArrayList<String> = ArrayList(itemList)
    private val selectedItems: HashSet<String> = HashSet()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewItem: TextView = itemView.findViewById(R.id.textView)
        private val checkBox: CheckBox = itemView.findViewById(R.id.checkbox)

        fun bind(item: String) {
            textViewItem.text = item

            // Handle checkbox state change
            checkBox.setOnCheckedChangeListener(null)
            checkBox.isChecked = item in selectedItems
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    // Add the item to selectedItems
                    selectedItems.add(item)
                } else {
                    // Remove the item from selectedItems
                    selectedItems.remove(item)
                }
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val filteredList: ArrayList<String> = ArrayList()
                val query = constraint.toString().toLowerCase(Locale.ROOT).trim()

                if (query.isEmpty()) {
                    filteredList.addAll(originalItemList)
                } else {
                    for (item in originalItemList) {
                        if (item.toLowerCase(Locale.ROOT).contains(query)) {
                            filteredList.add(item)
                        }
                    }
                }

                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                itemList.clear()
                itemList.addAll(results.values as ArrayList<String>)
                notifyDataSetChanged()
            }
        }
    }
}
