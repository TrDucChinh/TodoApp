package com.proptit.todoapp.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.proptit.todoapp.R
import com.proptit.todoapp.adapter.recyclerviewadapter.SubtasksAdapter
import com.proptit.todoapp.database.category.CategoryRepository
import com.proptit.todoapp.databinding.FragmentTaskDetailBinding
import com.proptit.todoapp.dialogfragment.CategoryPickerFragment
import com.proptit.todoapp.dialogfragment.CreateCategoryFragment
import com.proptit.todoapp.dialogfragment.PriorityPickerFragment
import com.proptit.todoapp.interfaces.ICategoryListener
import com.proptit.todoapp.interfaces.IPriorityListener
import com.proptit.todoapp.model.Category
import com.proptit.todoapp.model.Subtask
import com.proptit.todoapp.model.Task
import com.proptit.todoapp.utils.KeyBoard
import com.proptit.todoapp.utils.KeyBoard.onDone
import com.proptit.todoapp.utils.ListData
import com.proptit.todoapp.viewmodel.DetailTaskViewModel
import com.proptit.todoapp.viewmodel.HomeViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class TaskDetailFragment : Fragment() {
    private var selectedPriority = -1
    private var selectedCategory = -1
    private var dueDate: Date? = null
    private var dueTime: Date? = null

    private lateinit var task : Task
    private val args : TaskDetailFragmentArgs by navArgs()
    private lateinit var subtasksAdapter: SubtasksAdapter

    private var _binding: FragmentTaskDetailBinding? = null
    private val binding get() = _binding!!

    private val categoryRepository: CategoryRepository by lazy {
        CategoryRepository(requireActivity().application, ListData.categoryItems)
    }
    private val homeViewModel: HomeViewModel by activityViewModels() {
        HomeViewModel.HomeViewModelFactory(requireActivity().application)
    }


    private val taskDetailTaskViewModel by lazy {
        ViewModelProvider(
            this,
            DetailTaskViewModel.DetailTaskViewModelFactory(task)
        )[DetailTaskViewModel::class.java]
    }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTaskDetailBinding.inflate(inflater, container, false)
        setUpOnBackPress()
        return binding.root
    }

    private fun setUpOnBackPress() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            callback
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        task = args.taskArgs
        initComponent()
        initBehavior()
    }


    private fun initComponent() {
        subtasksAdapter = SubtasksAdapter(
            onUpdate = { position ->
                onUpdatedSubtask(position)
            },
            onRemove = { position ->
                onRemoveSubtask(position)
            }
        )

        subtasksAdapter.updateData(task.subTask)


        binding.apply {
            rvSubtask.adapter = subtasksAdapter
            rvSubtask.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            cbTask.isChecked = task.isFinish
            etTaskTitle.setText(task.title)
            etTaskDescription.setText(task.description)
            val date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(task.dueDate)
            val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(task.dueTime)
            tvDueDate.text = "$date $time"
            val category = categoryRepository.getCategoryById(task.categoryId)
            category.observe(viewLifecycleOwner) {
                tvCategory.text = "\t ${it.titleCategory}"
                tvCategory.setCompoundDrawablesWithIntrinsicBounds(it.idIcon, 0, 0, 0)
            }
            dueDate = task.dueDate
            dueTime = task.dueTime
            selectedCategory = task.categoryId
            selectedPriority = task.taskPriority
            tvPriority.text = "\t ${task.taskPriority}"
            tvPriority.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_flag, 0, 0, 0)


        }
    }

    private fun initBehavior() {
        binding.apply {
            toolbar.setNavigationOnClickListener {
                onBack()
            }

            edAddSubtask.onDone {
                KeyBoard.hideSoftKeyboard(binding.root, requireActivity())
                addSubtask()
            }
            taskDetailTaskViewModel._subTasks.observe(viewLifecycleOwner) {
                subtasksAdapter.updateData(it)
            }

            tvDueDate.setOnClickListener {
                setTime()
            }
            tvCategory.setOnClickListener {
                setCategory()
            }
            tvPriority.setOnClickListener {
                setPriority()
            }
            actionDelete.setOnClickListener {
                deleteTask()
            }

        }
    }

    private fun addSubtask() {
        val subtask = Subtask(binding.edAddSubtask.text.toString(), false)
        taskDetailTaskViewModel.addSubtask(subtask)
        binding.edAddSubtask.apply {
            text.clear()
            clearFocus()
        }
    }

    private fun deleteTask() {
        context?.let {
            AlertDialog.Builder(it)
                .setTitle("Delete task")
                .setMessage("Do you want to delete this task?")
                .setIcon(R.drawable.ic_delete)
                .setPositiveButton("Yes") { _, _ ->
                    homeViewModel.deleteTask(task)  // Delete the task
                    findNavController().popBackStack()  // Navigate back
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        } ?: Log.e("TaskDetailFragment", "deleteTask: An error has occurred!! Please restart the app.")
    }

    private fun onBack() {
        updateDetailTaskViewModel()
        if (taskDetailTaskViewModel.isChanged()) {
            context?.let {
                AlertDialog.Builder(it)
                    .setTitle("Save changes")
                    .setMessage("Do you want to save changes?")
                    .setIcon(R.drawable.ic_save)
                    .setPositiveButton("Yes") { _, _ ->
                        homeViewModel.updateTask(taskDetailTaskViewModel.getNewTask())
                        findNavController().popBackStack()
                    }
                    .setNegativeButton("No") { _, _ ->
                        findNavController().popBackStack()
                    }
                    .setNeutralButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            } ?: {
                Log.e(
                    "TaskDetailFragment",
                    "onBack: An error has occurred!! Please restart the app."
                )
            }
        } else {
            findNavController().popBackStack()
        }
    }

    private fun updateDetailTaskViewModel() {
        taskDetailTaskViewModel.apply {
            newTitle = binding.etTaskTitle.text.toString().trim()
            newDescription = binding.etTaskDescription.text.toString().trim()
            newIsFinished = binding.cbTask.isChecked
            newCategoryId = selectedCategory
            newTaskPriority = selectedPriority
            newDueDate = dueDate!!
            newDueTime = dueTime!!
        }
    }

    private fun onUpdatedSubtask(position: Int) {
        taskDetailTaskViewModel.onUpdatedSubtask(position)
    }

    private fun onRemoveSubtask(position: Int) {
        taskDetailTaskViewModel.onRemoveSubtask(position)
    }

    private fun setTime() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()
        datePicker.addOnPositiveButtonClickListener { selection ->
            dueDate = Date(selection)
            Log.e("AddTaskFragment", "onPositiveButtonClick: $dueDate")
            // Hiển thị TimePicker sau khi chọn ngày
            showTimePicker()
        }
        datePicker.show(childFragmentManager, "DATE_PICKER")
    }

    private fun showTimePicker() {
        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY))
            .setMinute(Calendar.getInstance().get(Calendar.MINUTE))
            .build()

        timePicker.addOnPositiveButtonClickListener {
            dueTime = SimpleDateFormat(
                "HH:mm",
                Locale.getDefault()
            ).parse("${timePicker.hour}:${timePicker.minute}")
            dueDate?.let {
                val date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(it)
                val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(dueTime)
                binding.tvDueDate.text = "$date $time"
            }
        }

        timePicker.show(childFragmentManager, "TIME_PICKER")
    }

    private fun setCategory() {
        val categoryPicker = CategoryPickerFragment(
            object : ICategoryListener {
                override fun onAddCategoryClick() {
                    val createCategoryFragment = CreateCategoryFragment()
                    createCategoryFragment.show(childFragmentManager, CreateCategoryFragment.TAG)
                    createCategoryFragment.setStyle(
                        DialogFragment.STYLE_NORMAL,
                        android.R.style.Theme_Black_NoTitleBar_Fullscreen
                    )
                }

                override fun onCategoryClick(category: Category) {
                    selectedCategory = category.id
                    binding.tvCategory.text = "\t ${category.titleCategory}"
                    binding.tvCategory.setCompoundDrawablesWithIntrinsicBounds(
                        category.idIcon,
                        0,
                        0,
                        0
                    )
                }
            },
            selectedCategory
        )
        categoryPicker.show(childFragmentManager, CategoryPickerFragment.TAG)
    }

    private fun setPriority() {
        val priorityPicker = PriorityPickerFragment(object : IPriorityListener {
            override fun onClickPriority(priority: Int) {
                priority.let {
                    selectedPriority = it
                    binding.tvPriority.text = "\t $it"
                }
            }
        }, selectedPriority)
        priorityPicker.show(childFragmentManager, PriorityPickerFragment.TAG)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}