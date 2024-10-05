package com.proptit.todoapp.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.proptit.todoapp.NavGraphDirections
import com.proptit.todoapp.R
import com.proptit.todoapp.adapter.CalendarAdapter
import com.proptit.todoapp.adapter.recyclerviewadapter.DayTaskRecyclerAdapter
import com.proptit.todoapp.database.category.CategoryRepository
import com.proptit.todoapp.database.task.TaskRepository
import com.proptit.todoapp.databinding.FragmentCalendarBinding
import com.proptit.todoapp.interfaces.ITaskListener
import com.proptit.todoapp.model.CalendarData
import com.proptit.todoapp.model.Task
import com.proptit.todoapp.utils.ListData
import com.proptit.todoapp.viewmodel.CalendarViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


class CalendarFragment : Fragment(), CalendarAdapter.CalendarInterface {
    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!
    private val sdf = SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
    private val cal = Calendar.getInstance()
    private var mStartD: Date? = null

    // Use MutableLiveData to observe changes in the selected date
    private val dayPick = MutableLiveData<Date>(Date())
    private val calendarAdapter = CalendarAdapter(this, arrayListOf())
    private val calendarList = arrayListOf<CalendarData>()

    private val categoryRepository: CategoryRepository by lazy {
        CategoryRepository(requireActivity().application, ListData.categoryItems)
    }
    private val taskRepository: TaskRepository by lazy {
        TaskRepository(requireActivity().application)
    }

    private val calendarViewModel : CalendarViewModel by lazy {
        CalendarViewModel(requireActivity().application)
    }

    private val taskListener = object : ITaskListener {
        override fun onTask(task: Task) {
            val action = NavGraphDirections.actionGlobalTaskDetailFragment(task)
            findNavController().navigate(action)
        }

        override fun onTaskStatusChange(task: Task) {
            calendarViewModel.updateTask(task)
        }
    }

    private val taskRecyclerAdapter : DayTaskRecyclerAdapter by lazy {
        DayTaskRecyclerAdapter(taskListener, categoryRepository, taskRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initCalendar()
        initComponent()
        observeDateSelection()
    }

    private fun initComponent(){
        binding.apply {
            rvTask.setHasFixedSize(true)
            rvTask.adapter = taskRecyclerAdapter
            rvTask.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    // Observe the selected day and fetch tasks for that date
    private fun observeDateSelection() {
        dayPick.observe(viewLifecycleOwner) { selectedDate ->
            calendarViewModel.getTaskByDate(selectedDate).observe(viewLifecycleOwner) { tasks ->
                taskRecyclerAdapter.submitList(tasks)
            }
        }
    }

    private fun init() {
        binding.apply {
            monthYearPicker.text = sdf.format(cal.time)
            rvCalendar.setHasFixedSize(true)
            rvCalendar.adapter = calendarAdapter
            monthYearPicker.setOnClickListener {
                displayDatePicker()
            }
        }
    }

    private fun initCalendar() {
        mStartD = Date()
        cal.time = Date()
        getDate()
    }

    private fun displayDatePicker() {
        val materialDateBuilder: MaterialDatePicker.Builder<Long> =
            MaterialDatePicker.Builder.datePicker()
        materialDateBuilder.setTitleText("Select Date")
        val materialDatePicker = materialDateBuilder.build()
        materialDatePicker.show(childFragmentManager, "DATE_PICKER")
        materialDatePicker.addOnPositiveButtonClickListener {
            mStartD = Date(it)
            binding.monthYearPicker.text = sdf.format(it)
            cal.time = Date(it)
            getDate()
        }
    }

    private fun getDate(){
        val dateList = arrayListOf<CalendarData>()
        val dates = ArrayList<Date>()
        val monthCalendar = cal.clone() as Calendar
        val maxDaysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1)

        while (dates.size < maxDaysInMonth) {
            dates.add(monthCalendar.time)
            dateList.add(CalendarData(monthCalendar.time))
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        calendarList.clear()
        calendarList.addAll(dateList)
        calendarAdapter.updateList(calendarList)

        // Scroll to the selected date in the calendar
        for (item in dateList.indices) {
            if (dateList[item].data == mStartD) {
                calendarAdapter.setPos(item)
                binding.rvCalendar.scrollToPosition(item)
            }
        }
    }

    // Update selected day and trigger task loading
    override fun onSelect(calendarData: CalendarData, position: Int, day: TextView) {
        dayPick.value = calendarData.data
        calendarList.forEachIndexed { index, data ->
            data.isSelected = index == position
        }
        calendarAdapter.updateList(calendarList)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
