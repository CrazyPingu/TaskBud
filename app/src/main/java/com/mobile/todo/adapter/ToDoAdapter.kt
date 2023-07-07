package com.mobile.todo.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mobile.todo.EditTodoHabit
import com.mobile.todo.HomePage
import com.mobile.todo.R
import com.mobile.todo.database.AppDatabase
import com.mobile.todo.database.dataset.Search
import com.mobile.todo.database.dataset.Tag
import com.mobile.todo.database.dataset.ToDo
import com.mobile.todo.utils.Constant
import com.mobile.todo.utils.Monet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ToDoAdapter(private var itemList: MutableList<ToDo>, private var searchList: MutableList<Search>) :
    RecyclerView.Adapter<ToDoAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context // Store the reference to the parent ViewGroup
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

        if(Constant.getMonet(context)){
            Monet.setCheckBoxMonet(holder.checkbox, context)
            Monet.setCheckBoxMonet(holder.starCheckBoxItem, context)
//            Monet.setDeleteButtonMonet(holder.delete, context)
        }

        val result = searchList.any { search ->
            search.toDoId == item.id && search.tag == Tag.FAV
        }

        if (result) {
            holder.starCheckBoxItem.isChecked = true
        }

        holder.delete.setOnClickListener {
            val dialog = MaterialAlertDialogBuilder(context)
                .setTitle("Delete to do")
                .setMessage("Do you want to delete this to do?")
                .setPositiveButton("OK") { dialog, _ ->
                    // Remove to do from database
                     GlobalScope.launch(Dispatchers.IO) {
                        AppDatabase.getDatabase(context).toDoDao().deleteToDo(item)
                        val size = AppDatabase.getDatabase(context).toDoDao().getAllToDoByUserId(HomePage.USER_ID).size
                         withContext(Dispatchers.Main){
                             if (size == 0) {
                                 (context as HomePage).findViewById<View>(R.id.no_result).visibility = View.VISIBLE
                             } else {
                                 (context as HomePage).findViewById<View>(R.id.no_result).visibility = View.GONE
                             }
                         }
                    }
                    val currentPosition = itemList.indexOf(item)
                    if (currentPosition != -1) {
                        itemList.removeAt(currentPosition)
                        notifyItemRemoved(currentPosition)
                    }
                    Constant.refreshWidget(context)
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()

            dialog.show()

            if(Constant.getMonet(context)) {
                Monet.setButtonTextMonet(dialog.getButton(AlertDialog.BUTTON_POSITIVE), context)
                Monet.setButtonTextMonet(dialog.getButton(AlertDialog.BUTTON_NEGATIVE), context)
            }


        }

        holder.textView.setOnClickListener {
            startActivity(
                context,
                Intent(
                    EditTodoHabit.newInstance(
                        context, EditTodoHabit.Companion.TYPE.TODO, item.id
                    )
                ), null
            )
        }

        holder.checkbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                 GlobalScope.launch(Dispatchers.IO) {
                    AppDatabase.getDatabase(context).toDoDao()
                        .setCompleted(item.id, isChecked)
                }
                holder.textView.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                 GlobalScope.launch(Dispatchers.IO) {
                    AppDatabase.getDatabase(context).toDoDao()
                        .setCompleted(item.id, isChecked)
                }
                holder.textView.paintFlags =
                    holder.textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }

            Constant.refreshWidget(context)
        }

        holder.starCheckBoxItem.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                 GlobalScope.launch(Dispatchers.IO) {
                    AppDatabase.getDatabase(context).searchDao()
                        .insertSearch(Search(item.id,  Tag.FAV))
                }
            } else {
                 GlobalScope.launch(Dispatchers.IO) {
                    AppDatabase.getDatabase(context).searchDao()
                        .removeTagFromToDoId(item.id, Tag.FAV)
                }
            }

            Constant.refreshWidget(context)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkbox: CheckBox = itemView.findViewById(R.id.checkbox)
        val textView: TextView = itemView.findViewById(R.id.textView)
        val delete: ImageView = itemView.findViewById(R.id.delete)
        val starCheckBoxItem: CheckBox = itemView.findViewById(R.id.starCheckBoxItem)
    }
}
