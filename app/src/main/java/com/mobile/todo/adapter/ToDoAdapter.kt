package com.mobile.todo.adapter

import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.mobile.todo.EditTodoHabit
import com.mobile.todo.R
import com.mobile.todo.database.AppDatabase
import com.mobile.todo.database.dataset.ToDo
import com.mobile.todo.utils.Constant
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TodoAdapter(private var itemList: MutableList<ToDo>) :
    RecyclerView.Adapter<TodoAdapter.ViewHolder>() {

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
        if (item.completed) {
            holder.checkbox.isChecked = true
            holder.textView.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }

        holder.delete.setOnClickListener {
            GlobalScope.launch {
                AppDatabase.getDatabase(context.context).toDoDao().deleteToDo(item)
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
                        context.context, EditTodoHabit.Companion.TYPE.TODO, item.id
                    )
                ), null
            )
        }

        holder.checkbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                GlobalScope.launch {
                    AppDatabase.getDatabase(context.context).toDoDao()
                        .setCompleted(item.id, isChecked)
                }
                holder.textView.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                GlobalScope.launch {
                    AppDatabase.getDatabase(context.context).toDoDao()
                        .setCompleted(item.id, isChecked)
                }
                holder.textView.paintFlags =
                    holder.textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
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
