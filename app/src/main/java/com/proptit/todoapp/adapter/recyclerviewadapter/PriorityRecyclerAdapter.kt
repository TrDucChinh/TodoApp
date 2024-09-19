package com.proptit.todoapp.adapter.recyclerviewadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.proptit.todoapp.R
import com.proptit.todoapp.interfaces.IPriorityListener
import com.proptit.todoapp.item.ItemPriority
import com.proptit.todoapp.utils.List

class PriorityRecyclerAdapter(
    private val priorityListener: IPriorityListener,
    private val selectedPriority: Int
) : RecyclerView.Adapter<PriorityRecyclerAdapter.PriorityViewHolder>() {
    private val priorityItems = List.priorityItems
    private var selectedPosition = -1;
    init {//Tìm vị trí đã được chọn
        selectedPosition = priorityItems.indexOfFirst { it.priority == selectedPriority }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PriorityViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_priority, parent, false)
        return PriorityViewHolder(view)
    }

    override fun onBindViewHolder(holder: PriorityViewHolder, position: Int) {
        holder.bind(priorityItems[position])
        if (position == selectedPosition) {
            holder.itemView.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.light_purple
                )
            )
        } else {
            holder.itemView.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.defaultColor
                )
            )
        }
        holder.itemView.setOnClickListener {
            val previousPosition = selectedPosition
            selectedPosition = position
            priorityListener.onClickPriority(priorityItems[position].priority)
            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)
        }
    }

    override fun getItemCount(): Int = priorityItems.size

    class PriorityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val priorityView = itemView.findViewById<TextView>(R.id.priority_item)
        fun bind(item: ItemPriority) {
            priorityView.text = item.priority.toString()
        }
    }
}