package com.proptit.todoapp.adapter.recyclerviewadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.proptit.todoapp.R
import com.proptit.todoapp.interfaces.IIconCategoryListener
import com.proptit.todoapp.item.ItemIcon
import com.proptit.todoapp.utils.List

class IconCategoryRecyclerAdapter(
    private val iconCategoryListener: IIconCategoryListener,
    private val selectedIconCategory: Int
): RecyclerView.Adapter<IconCategoryRecyclerAdapter.IconCategoryViewHolder>() {
    private val iconCategoryItems = List.iconCategoryItems
    private var selectedPosition = -1
    init {
        selectedPosition = iconCategoryItems.indexOfFirst { it.icon == selectedIconCategory }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconCategoryViewHolder {
        val  view = LayoutInflater.from(parent.context).inflate(R.layout.item_icon_category, parent, false)
        return IconCategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: IconCategoryViewHolder, position: Int) {
        holder.bind(iconCategoryItems[position])
        if (position == selectedPosition){
            holder.itemView.setBackgroundResource(R.color.light_purple)
        } else {
            holder.itemView.setBackgroundResource(R.color.defaultColor)
        }
        holder.itemView.setOnClickListener {
            val previousPosition = selectedPosition
            selectedPosition = position
            iconCategoryListener.onIconClick(iconCategoryItems[position].icon)
            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)
        }
    }

    override fun getItemCount(): Int {
        return iconCategoryItems.size
    }


    inner class IconCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val iconCategoryView = itemView.findViewById<ImageView>(R.id.icon_category)
        fun bind(item : ItemIcon){
            iconCategoryView.setImageResource(item.icon)
        }

    }
}