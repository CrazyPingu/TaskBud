package com.mobile.todo.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.mobile.todo.EditTodoHabit
import com.mobile.todo.HomePage
import com.mobile.todo.R
import com.mobile.todo.database.AppDatabase
import com.mobile.todo.database.dataset.Habit
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CustomAdapter(private var itemList: MutableList<Habit>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    private lateinit var context: ViewGroup

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent // Store the reference to the parent ViewGroup
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.textView.text = item.title
        holder.delete.setOnClickListener {
            GlobalScope.launch {
                AppDatabase.getDatabase(context.context).habitDao().deleteHabit(item)
            }
            val currentPosition = itemList.indexOf(item)
            if (currentPosition != -1) {
                itemList.removeAt(currentPosition)
                notifyItemRemoved(currentPosition)
            }
        }

        holder.textView.setOnClickListener {
            startActivity(
                context.context,
                Intent(
                    EditTodoHabit.newInstance(
                        context.context, EditTodoHabit.Companion.TYPE.HABIT, item.id
                    )
                ), null
            )
        }
    }


    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkbox: CheckBox = itemView.findViewById(R.id.checkbox)
        val textView: TextView = itemView.findViewById(R.id.textView)
        val delete: ImageView = itemView.findViewById(R.id.delete)
    }
}
