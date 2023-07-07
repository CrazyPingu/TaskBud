package com.mobile.todo.adapter

import android.app.AlertDialog
import android.content.Context
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mobile.todo.EditTodoHabit
import com.mobile.todo.HomePage
import com.mobile.todo.R
import com.mobile.todo.database.AppDatabase
import com.mobile.todo.database.dataset.Habit
import com.mobile.todo.fragment.HabitPage
import com.mobile.todo.utils.Constant
import com.mobile.todo.utils.Monet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HabitAdapter(private var itemList: MutableList<Habit>) :
    RecyclerView.Adapter<HabitAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context // Store the reference to the parent ViewGroup
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.textView.text = item.title
        holder.starCheckBoxItem.visibility = View.GONE

        if (item.lastDayCompleted == Constant.getCurrentDate()) {
            holder.checkbox.isChecked = true
            holder.textView.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }

        if (Constant.getMonet(context)) {
            Monet.setCheckBoxMonet(holder.checkbox, context)
            Monet.setCheckBoxMonet(holder.starCheckBoxItem, context)
//            Monet.setDeleteButtonMonet(holder.delete, context)
        }

        holder.delete.setOnClickListener {
            val dialog = MaterialAlertDialogBuilder(context).setTitle("Delete habit")
                .setMessage("Do you want to delete this habit?")
                .setPositiveButton("OK") { dialog, _ ->
                    // Remove habit from database
                    GlobalScope.launch(Dispatchers.IO) {
                        AppDatabase.getDatabase(context).habitDao().deleteHabit(item)
                        val size = AppDatabase.getDatabase(context).habitDao().getHabitsByUserId(HomePage.USER_ID).size
                        withContext(Dispatchers.Main) {
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
                    dialog.dismiss()
                }.setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }.create()

            dialog.show()

            if (Constant.getMonet(context)) {
                Monet.setButtonTextMonet(dialog.getButton(AlertDialog.BUTTON_POSITIVE), context)
                Monet.setButtonTextMonet(dialog.getButton(AlertDialog.BUTTON_NEGATIVE), context)
            }
        }

        holder.textView.setOnClickListener {
            startActivity(
                context, Intent(
                    EditTodoHabit.newInstance(
                        context, EditTodoHabit.Companion.TYPE.HABIT, item.id
                    )
                ), null
            )
        }

        holder.checkbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                 GlobalScope.launch(Dispatchers.IO) {
                    AppDatabase.getDatabase(context).habitDao()
                        .increaseStreak(item.id, Constant.getCurrentDate())
                }
                holder.textView.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                 GlobalScope.launch(Dispatchers.IO) {
                    AppDatabase.getDatabase(context).habitDao().resetLastDay(item.id)
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
        val starCheckBoxItem: CheckBox = itemView.findViewById(R.id.starCheckBoxItem)
    }
}
