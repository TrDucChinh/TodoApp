package com.proptit.todoapp.adapter.recyclerviewadapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.proptit.todoapp.databinding.ItemStubtaskBinding
import com.proptit.todoapp.model.Subtask

class SubtasksAdapter(
    private val onUpdate : (Int) -> Unit,
    private val onRemove : (Int) -> Unit
) : ListAdapter<Subtask, SubtasksAdapter.SubtasksViewHolder>(DiffCallback) {

    inner class SubtasksViewHolder(private val binding : ItemStubtaskBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(subtask: Subtask){
            binding.apply {
                tvSubtaskName.text = subtask.title
                cbSubtaskStatus.isChecked = subtask.isFinish
                cbSubtaskStatus.setOnClickListener {
                    onUpdate(adapterPosition)
                }
                ivRemoveSubtask.setOnClickListener {
                    onRemove(adapterPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubtasksViewHolder {
        return SubtasksViewHolder(ItemStubtaskBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    fun updateData(list : List<Subtask>){
        submitList(list.toList())
    }

    override fun onBindViewHolder(holder: SubtasksViewHolder, position: Int) {
        val current = getItem(position)
        Log.d("SubtaskAdapter", "onBindViewHolder: ${current.title}")
        holder.bind(current)
    }
    companion object{
        private val DiffCallback = object : DiffUtil.ItemCallback<Subtask>(){
            override fun areItemsTheSame(oldItem: Subtask, newItem: Subtask): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: Subtask, newItem: Subtask): Boolean {
                return (oldItem.isFinish == newItem.isFinish) && (oldItem.title == newItem.title)
            }
        }
    }
}