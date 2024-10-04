package com.proptit.todoapp.adapter.recyclerviewadapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.proptit.todoapp.R
import com.proptit.todoapp.databinding.ItemAddCategoryBinding
import com.proptit.todoapp.databinding.ItemCategoryBinding
import com.proptit.todoapp.diffcallback.CategoryDiffCallBack
import com.proptit.todoapp.interfaces.ICategoryListener
import com.proptit.todoapp.model.Category

class CategoryRecyclerAdapter(
    private val categoryListener: ICategoryListener,
//    private val selectedCategory: Int,
) : ListAdapter<Category, RecyclerView.ViewHolder>(CategoryDiffCallBack()) {
    companion object {
        private const val TYPE_CATEGORY = 0
        private const val TYPE_ADD_CATEGORY = 1
    }

    override fun getItemViewType(position: Int): Int {
        Log.d("CategoryRecyclerAdapter", "getItemViewType: $position")
        return if (position == currentList.size - 1) {
            TYPE_ADD_CATEGORY
        } else {
            TYPE_CATEGORY
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_ADD_CATEGORY) {
            val binding = ItemAddCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            AddCategoryViewHolder(binding)
        } else {
            val binding = ItemCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            CategoryViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CategoryViewHolder) {
            holder.bind(getItem(position))
        } else if (holder is AddCategoryViewHolder) {
            holder.bind()
        }
    }

//    override fun getItemCount(): Int {
//        return super.getItemCount() + 1
//    }


    inner class CategoryViewHolder(
        private val binding: ItemCategoryBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Category) {
            binding.apply {
//                Log.d("CategoryRecyclerAdapter", "bind: $category")
                categoryTitle.text = category.titleCategory
                categoryIcon.setImageResource(category.idIcon)
                categoryIcon.setBackgroundColor(ContextCompat.getColor(root.context, category.idColor))
                root.setOnClickListener {
                    categoryListener.onCategoryClick(category)
                }
            }
        }
    }

    inner class AddCategoryViewHolder(
        private val binding: ItemAddCategoryBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ResourceAsColor")
        fun bind() {
            binding.apply {
                categoryTitle.text = "Create New"
                categoryIcon.setImageResource(R.drawable.category_add)
                categoryIcon.setBackgroundColor(ContextCompat.getColor(root.context, R.color.cyan))

                root.setOnClickListener {
                    categoryListener.onAddCategoryClick()
                }
            }
        }
    }


}

