package com.proptit.todoapp.adapter.recyclerviewadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.proptit.todoapp.R
import com.proptit.todoapp.interfaces.IColorListener
import com.proptit.todoapp.utils.ListData

class ColorCategoryRecyclerAdapter(
    private val colorCategoryListener: IColorListener,
) :
    RecyclerView.Adapter<ColorCategoryRecyclerAdapter.ColorCategoryViewHolder>() {
    private val colorCategoryItems = ListData.colorCategoryItems
    private var selectedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorCategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category_color, parent, false)
        return ColorCategoryViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ColorCategoryViewHolder,
        position: Int,
    ) {
        holder.circleView.setBackgroundResource(colorCategoryItems[position].color)
        if (position == selectedPosition) {
            holder.checkMark.visibility = View.VISIBLE
        } else {
            holder.checkMark.visibility = View.GONE
        }
        holder.itemView.setOnClickListener {
            val previousPosition = selectedPosition
            selectedPosition = position
            colorCategoryListener.onColorClick(colorCategoryItems[position].color)
            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)
        }
    }

    override fun getItemCount(): Int {
        return colorCategoryItems.size
    }

    inner class ColorCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val circleView: View = itemView.findViewById(R.id.circleView)
        val checkMark: ImageView = itemView.findViewById(R.id.checkMark)
    }
}


