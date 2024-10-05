package com.proptit.todoapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.MaterialDatePicker
import com.proptit.todoapp.R
import com.proptit.todoapp.databinding.DateItemBinding
import com.proptit.todoapp.model.CalendarData

class CalendarAdapter(
    private val calendarInterface: CalendarInterface,
    private val list: ArrayList<CalendarData>,

    ) : RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {
    private var pos = -1
    interface CalendarInterface {
        fun onSelect(calendarData: CalendarData, position: Int, day: TextView)
    }
    inner class ViewHolder(private val binding: DateItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(calendarDataModel: CalendarData, position: Int) {
            val calendarDay = binding.tvCalendarDay
            val calendarDate = binding.tvCalendarDate
            val cardView = binding.root
            if (pos == position) {
                calendarDataModel.isSelected = true
            }
            if (calendarDataModel.isSelected) {
                pos = -1
                calendarDay.setTextColor(
                    ContextCompat.getColor(itemView.context, R.color.white)
                )
                calendarDate.setTextColor(
                    ContextCompat.getColor(itemView.context, R.color.white)
                )
                cardView.setCardBackgroundColor(
                    ContextCompat.getColor(itemView.context, R.color.teal)
                )
            } else {
                calendarDay.setTextColor(
                    ContextCompat.getColor(itemView.context, R.color.teal)
                )
                calendarDate.setTextColor(
                    ContextCompat.getColor(itemView.context, R.color.teal)
                )
                cardView.setCardBackgroundColor(
                    ContextCompat.getColor(itemView.context, R.color.white)
                )
            }
            calendarDay.text = calendarDataModel.calendarDay
            calendarDate.text = calendarDataModel.calendarDate
            cardView.setOnClickListener {
                calendarInterface.onSelect(calendarDataModel, adapterPosition, calendarDate)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DateItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], position)
    }

    fun setPos(pos: Int) {
        this.pos = pos
    }

    fun updateList(calendarList: ArrayList<CalendarData>) {
        this.list.clear()
        this.list.addAll(calendarList)
        notifyDataSetChanged()
    }
}