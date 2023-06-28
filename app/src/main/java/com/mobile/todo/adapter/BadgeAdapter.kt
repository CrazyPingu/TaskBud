package com.mobile.todo.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobile.todo.R
import com.mobile.todo.adapter.BadgeContainer

class BadgeAdapter(private var itemList: MutableList<BadgeContainer>) :
    RecyclerView.Adapter<BadgeAdapter.ViewHolder>() {

    private lateinit var context: ViewGroup

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent // Store the reference to the parent ViewGroup
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_badge, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        if(!item.obtained){
            holder.image.setImageURI(Constant.LOCKED_ICON)
        }else{
            holder.image.setImageURI(item.icon)
        }
        holder.title.text = item.name
        holder.description.text = item.description
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.image_badge)
        val title: TextView = itemView.findViewById(R.id.title_badge)
        val description: TextView = itemView.findViewById(R.id.description_badge)
    }
}
