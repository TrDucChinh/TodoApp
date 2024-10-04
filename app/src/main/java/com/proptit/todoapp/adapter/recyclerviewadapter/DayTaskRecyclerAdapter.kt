package com.proptit.todoapp.adapter.recyclerviewadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.proptit.todoapp.database.category.CategoryRepository
import com.proptit.todoapp.database.task.TaskRepository
import com.proptit.todoapp.databinding.ItemTaskBinding
import com.proptit.todoapp.diffcallback.TaskDiffCallBack
import com.proptit.todoapp.interfaces.ITaskListener
import com.proptit.todoapp.model.Category
import com.proptit.todoapp.model.Task
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class DayTaskRecyclerAdapter(
    private val taskListener: ITaskListener,
    private val categoryRepository: CategoryRepository,
    private val taskRepository: TaskRepository,
) : ListAdapter<Task, DayTaskRecyclerAdapter.TaskViewHolder>(TaskDiffCallBack()) {

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TaskViewHolder(binding, categoryRepository)
    }

    inner class TaskViewHolder(
        private val binding: ItemTaskBinding,
        private val categoryRepository: CategoryRepository,
    ) : RecyclerView.ViewHolder(binding.root) {

        private var currentCategoryId: Int? = null
        private var categoryObserver: Observer<Category>? = null

        fun bind(task: Task) {
            binding.apply {
                tvTaskTitle.text = task.title

                // Lấy và format ngày giờ
                val date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(task.dueDate)
                val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(task.dueTime)
                tvTaskDescription.text = "$date $time"

                // Lưu categoryId hiện tại để theo dõi sự thay đổi
                currentCategoryId = task.categoryId

                // Hủy observer nếu có
                categoryObserver?.let {
                    categoryRepository.getCategoryById(currentCategoryId ?: 0).removeObserver(it)
                }

                // Thiết lập observer cho category
                categoryObserver = Observer<Category> { category ->
                    tvCategory.text = "\t ${category.titleCategory}"
                    tvCategory.setCompoundDrawablesWithIntrinsicBounds(
                        category.idIcon,
                        0,
                        0,
                        0
                    )
                }

                // Quan sát category
                categoryRepository.getCategoryById(task.categoryId)
                    .observe(binding.root.context as LifecycleOwner, categoryObserver!!)

                tvTaskPriority.text = "\t ${task.taskPriority}"

                // Thiết lập lại listener trước khi cập nhật trạng thái
                cbTask.setOnCheckedChangeListener(null) // Đặt listener là null trước
                cbTask.isChecked = task.isFinish // Cập nhật trạng thái

                // Sử dụng setOnCheckedChangeListener một cách an toàn
                cbTask.setOnCheckedChangeListener { _, isChecked ->
                    if (task.isFinish != isChecked) { // Chỉ gọi khi giá trị thay đổi
                        task.isFinish = isChecked
                        taskListener.onTaskStatusChange(task) // Gọi listener

                        // Gọi updateTask trong một coroutine
                        val context = binding.root.context
                        if (context is LifecycleOwner) {
                            context.lifecycleScope.launch {
                                taskRepository.updateTask(task) // Cập nhật trạng thái trong cơ sở dữ liệu
                                // Thông báo cho adapter rằng item đã thay đổi
                                notifyItemChanged(adapterPosition)
                            }
                        }
                    }
                }

                // Xử lý click trên item
                root.setOnClickListener {
                    taskListener.onTask(task)
                }
            }
        }
    }
}