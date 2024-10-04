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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.proptit.todoapp.R
import com.proptit.todoapp.database.category.CategoryRepository
import com.proptit.todoapp.databinding.FragmentTaskDetailBinding
import com.proptit.todoapp.dialogfragment.CategoryPickerFragment
import com.proptit.todoapp.dialogfragment.CreateCategoryFragment
import com.proptit.todoapp.dialogfragment.PriorityPickerFragment
import com.proptit.todoapp.interfaces.ICategoryListener
import com.proptit.todoapp.interfaces.IPriorityListener
import com.proptit.todoapp.model.Category
import com.proptit.todoapp.model.Task
import com.proptit.todoapp.utils.ListData
import com.proptit.todoapp.viewmodel.DetailTaskViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class TaskDetailFragment : Fragment() {
    private var selectedPriority = -1
    private var selectedCategory = -1
    private var dueDate: Date? = null
    private var dueTime: Date? = null
    private var _binding: FragmentTaskDetailBinding? = null
    private val binding get() = _binding!!
    private val categoryRepository: CategoryRepository by lazy {
        CategoryRepository(requireActivity().application, ListData.categoryItems)
    }
    private val taskDetailTaskViewModel: DetailTaskViewModel by activityViewModels {
        DetailTaskViewModel.DetailTaskViewModelFactory(requireActivity().application)
    }
    private val args by navArgs<TaskDetailFragmentArgs>()
    private val task by lazy {
        taskDetailTaskViewModel.getTaskById(args.taskId)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponent()
        initBehavior()
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

    private fun initComponent() {
        binding.apply {

            // Init data

            task.observe(viewLifecycleOwner) {
                cbTask.isChecked = it.isFinish
                etTaskTitle.setText(it.title)
                etTaskDescription.setText(it.description)
                val date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(it.dueDate)
                val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(it.dueTime)
                tvDueDate.text = "$date $time"
                val category = categoryRepository.getCategoryById(it.categoryId)
                category.observe(viewLifecycleOwner) {
                    tvCategory.text = "\t ${it.titleCategory}"
                    tvCategory.setCompoundDrawablesWithIntrinsicBounds(it.idIcon, 0, 0, 0)
                }
                dueDate = it.dueDate
                dueTime = it.dueTime
                selectedCategory = it.categoryId
                selectedPriority = it.taskPriority
                tvPriority.text = "\t ${it.taskPriority}"
                tvPriority.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_flag, 0, 0, 0)
            }

        }
    }

    private fun initBehavior() {
        binding.apply {
            toolbar.setNavigationOnClickListener {
                onBack()
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

    private fun deleteTask(){
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Task")
            .setMessage("Do you want to delete this task?")
            .setPositiveButton("Yes") { dialog, which ->
                taskDetailTaskViewModel.deleteTask(task.value!!)
                findNavController().navigateUp()
            }
            .setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
    private fun onBack() {
        AlertDialog.Builder(requireContext())
            .setTitle("Update Task")
            .setMessage("Do you want to update this task?")
            .setPositiveButton("Yes") { dialog, which ->
                taskDetailTaskViewModel.updateTask(
                    task.value!!.copy(
                        title = binding.etTaskTitle.text.toString(),
                        description = binding.etTaskDescription.text.toString(),
                        dueDate = dueDate!!,
                        dueTime = dueTime!!,
                        categoryId = selectedCategory,
                        taskPriority = selectedPriority,
                        isFinish = binding.cbTask.isChecked
                    )
                )
                findNavController().navigateUp()
            }
            .setNegativeButton("No") { dialog, which ->
                findNavController().navigateUp()
            }
            .create()
            .show()
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