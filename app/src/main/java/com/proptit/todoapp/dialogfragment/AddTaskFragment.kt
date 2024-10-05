package com.proptit.todoapp.dialogfragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.proptit.todoapp.databinding.FragmentAddTaskBinding
import com.proptit.todoapp.interfaces.ICategoryListener
import com.proptit.todoapp.interfaces.IPriorityListener
import com.proptit.todoapp.model.Category
import com.proptit.todoapp.model.Task
import com.proptit.todoapp.testnoti.Notification
import com.proptit.todoapp.viewmodel.AddTaskViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddTaskFragment() : BottomSheetDialogFragment() {
    private var _binding: FragmentAddTaskBinding? = null
    private val binding get() = _binding!!
    private var selectedPriority = -1
    private var selectedCategory = -1
    private var dueDate: Date? = null
    private var dueTime: Date? = null
    private val addTaskViewModel: AddTaskViewModel by activityViewModels {
        AddTaskViewModel.AddTaskViewModelFactory(requireActivity().application)
    }
    private var hour = 0
    private var minute = 0

    companion object {
        const val TAG = "AddTaskFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBehavior()

    }

    private fun initBehavior() {
        binding.apply {
            btnSetTime.setOnClickListener {
                setTime()
            }
            btnSetPriority.setOnClickListener {
                setPriority()
            }
            btnSetCategory.setOnClickListener {
                setCategory()
            }
            btnSend.setOnClickListener {
                addTaskViewModel.insertTask(
                    etTaskName.text.toString(),
                    etTaskDescription.text.toString(),
                    dueDate!!,
                    dueTime!!,
                    selectedCategory,
                    false,
                    selectedPriority
                )
                val task = Task(
                    title = etTaskName.text.toString(),
                    description = etTaskDescription.text.toString(),
                    dueDate = dueDate!!,
                    dueTime = dueTime!!,
                    categoryId = selectedCategory,
                    isFinish = false,
                    taskPriority = selectedPriority
                )
                Notification.notificationTask(requireContext(), task)
                dismiss()
            }

        }
    }

    private fun setPriority() {
        val priorityPicker = PriorityPickerFragment(object : IPriorityListener {
            override fun onClickPriority(priority: Int) {
                priority.let {
                    selectedPriority = it
                    println(it)
                }
            }
        }, selectedPriority)
        priorityPicker.show(childFragmentManager, PriorityPickerFragment.TAG)
    }

    private fun setTime() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()
        datePicker.addOnPositiveButtonClickListener { selection ->
//            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
//            val date = Date(selection)
//            Log.e("AddTaskFragment", "onPositiveButtonClick: $date")
//            val formattedDate = sdf.format(date)
//            println(formattedDate)
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
            dueTime = SimpleDateFormat("HH:mm", Locale.getDefault()).parse("${timePicker.hour}:${timePicker.minute}")

            Log.e("AddTaskFragment", "onPositiveButtonClick: $dueTime")

        }

        timePicker.show(childFragmentManager, "TIME_PICKER")
    }

    private fun setCategory() {
        val categoryPicker = CategoryPickerFragment(
            object : ICategoryListener {
                override fun onAddCategoryClick() {
                    val createCategoryFragment = CreateCategoryFragment()
                    createCategoryFragment.show(
                        childFragmentManager,
                        CreateCategoryFragment.TAG
                    )
                    createCategoryFragment.setStyle(
                        DialogFragment.STYLE_NORMAL,
                        android.R.style.Theme_Black_NoTitleBar_Fullscreen
                    )
                }

                override fun onCategoryClick(category: Category) {
                    selectedCategory = category.id
                    println(selectedCategory)
                    println(category)
                }
            },
            selectedCategory
        )
        categoryPicker.show(childFragmentManager, CategoryPickerFragment.TAG)
//        categoryPicker.setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Panel)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}