package com.proptit.todoapp.dialogfragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.proptit.todoapp.databinding.FragmentAddTaskBinding
import com.proptit.todoapp.interfaces.ICategoryListener
import com.proptit.todoapp.interfaces.IPriorityListener
import com.proptit.todoapp.model.Category
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddTaskFragment() : BottomSheetDialogFragment() {
    private var _binding: FragmentAddTaskBinding? = null
    private val binding get() = _binding!!
    private var selectedPriority = -1
    private var selectedCategory = -1
    private var hour = 0
    private var minute = 0

    companion object {
        const val TAG = "AddTaskFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
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
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = Date(selection)
            val formattedDate = sdf.format(date)
            println(formattedDate)
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
            val hour = timePicker.hour
            val minute = timePicker.minute
            println("$hour:$minute") // In ra giờ và phút đã chọn
        }

        timePicker.show(childFragmentManager, "TIME_PICKER")
    }

    private fun setCategory() {
        val categoryPicker = CategoryPickerFragment(
            object : ICategoryListener {
                override fun onAddCategoryClick() {
                   val createCategoryFragment = CreateCategoryFragment()
                    createCategoryFragment.show(childFragmentManager, CreateCategoryFragment.TAG)
                    createCategoryFragment.setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
                }

                override fun onCategoryClick(category: Category) {
                    TODO("Not yet implemented")
                }
            },
            /*selectedCategory*/
        )
        categoryPicker.show(childFragmentManager, CategoryPickerFragment.TAG)
//        categoryPicker.setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Panel)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}