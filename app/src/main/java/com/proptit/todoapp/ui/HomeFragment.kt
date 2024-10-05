package com.proptit.todoapp.ui

import android.R
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.proptit.todoapp.NavGraphDirections
import com.proptit.todoapp.adapter.recyclerviewadapter.DayTaskRecyclerAdapter
import com.proptit.todoapp.database.category.CategoryRepository
import com.proptit.todoapp.database.task.TaskRepository
import com.proptit.todoapp.databinding.FragmentHomeBinding
import com.proptit.todoapp.interfaces.ITaskListener
import com.proptit.todoapp.model.Task
import com.proptit.todoapp.utils.ListData
import com.proptit.todoapp.viewmodel.HomeViewModel

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val categoryRepository: CategoryRepository by lazy {
        CategoryRepository(requireActivity().application, ListData.categoryItems)
    }
    private val taskRepository: TaskRepository by lazy {
        TaskRepository(requireActivity().application)
    }

    private val taskListener = object : ITaskListener {
        override fun onTask(task: Task) {
            val action = NavGraphDirections.actionGlobalTaskDetailFragment(task)
            findNavController().navigate(action)
        }

        override fun onTaskStatusChange(task: Task) {
            homeViewModel.updateTask(task)
            loadDataTask(selectedDayTask ?: "Today")  // Cập nhật lại danh sách task hàng ngày
            loadDataProgress(
                selectedProgressTask ?: "On Progress"
            )  // Cập nhật lại danh sách task tiến độ
        }
    }

    private val dayTaskRecyclerAdapter: DayTaskRecyclerAdapter by lazy {
        DayTaskRecyclerAdapter(taskListener, categoryRepository, taskRepository)
    }
    private val progressTaskAdapter: DayTaskRecyclerAdapter by lazy {
        DayTaskRecyclerAdapter(taskListener, categoryRepository, taskRepository)
    }

    private val homeViewModel: HomeViewModel by activityViewModels {
        HomeViewModel.HomeViewModelFactory(requireActivity().application)
    }

    private var dayTaskObserver: Observer<List<Task>>? = null
    private var progressTaskObserver: Observer<List<Task>>? = null
    private var selectedDayTask: String? = null
    private var selectedProgressTask: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            selectedDayTask = savedInstanceState.getString("selectedDayTask", "Today")
            selectedProgressTask =
                savedInstanceState.getString("selectedProgressTask", "On Progress")
        }
        initComponent()
    }

    private fun initComponent() {
        setupAdapters()
        setupRecyclerViews()

        homeViewModel.getAllTask().observe(viewLifecycleOwner) { tasks ->
            if (tasks.isEmpty()) {
                binding.allEmptyTask.visibility = View.VISIBLE
                binding.clTask.visibility = View.GONE
                binding.rvDayTask.visibility = View.GONE
                binding.rvDayProgress.visibility = View.GONE
                binding.emptyTask.visibility = View.GONE
                binding.emptyTaskProgress.visibility = View.GONE
            } else {
                binding.clTask.visibility = View.VISIBLE
                binding.allEmptyTask.visibility = View.GONE
                setupAutoCompleteTextViews()
            }
        }
    }

    private fun setupAdapters() {
        val arrayDayAdapter = ArrayAdapter(
            requireContext(),
            R.layout.simple_spinner_dropdown_item,
            ListData.dayDropDownItem
        )
        val arrayProgressionAdapter = ArrayAdapter(
            requireContext(),
            R.layout.simple_spinner_dropdown_item,
            ListData.progressionDropDownItem
        )

        binding.actvDayTask.setAdapter(arrayDayAdapter)
        binding.actvProgress.setAdapter(arrayProgressionAdapter)
    }

    private fun setupRecyclerViews() {
        binding.rvDayTask.apply {
            adapter = dayTaskRecyclerAdapter
            layoutManager = LinearLayoutManager(context)
        }

        binding.rvDayProgress.apply {
            adapter = progressTaskAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setupAutoCompleteTextViews() {
        // Day Task
        binding.actvDayTask.setOnItemClickListener { parent, _, position, _ ->
            selectedDayTask = parent.getItemAtPosition(position) as String
            loadDataTask(selectedDayTask ?: "Today")
        }

        // Progress Task
        binding.actvProgress.setOnItemClickListener { parent, _, position, _ ->
            selectedProgressTask = parent.getItemAtPosition(position) as String
            loadDataProgress(selectedProgressTask ?: "On Progress")
        }

        // Set initial text and load data
        setupAutoCompleteTextViewTask()
        setupAutoCompleteTextViewProgress()
    }

    private fun setupAutoCompleteTextViewTask() {
        val defaultItem = selectedDayTask ?: "Today"
        binding.actvDayTask.setText(defaultItem, false)
        loadDataTask(defaultItem)
    }

    private fun setupAutoCompleteTextViewProgress() {
        val defaultItem = selectedProgressTask ?: "On Progress"
        binding.actvProgress.setText(defaultItem, false)
        loadDataProgress(defaultItem)
    }

    private fun loadDataTask(selectedItem: String) {
        dayTaskObserver?.let {
            when (selectedItem) {
                "Today" -> homeViewModel.getAllTaskToday().removeObserver(it)
                "Tomorrow" -> homeViewModel.getAllTaskTomorrow().removeObserver(it)
                "Week" -> homeViewModel.getAllTaskWeek().removeObserver(it)
                "Month" -> homeViewModel.getAllTaskMonth().removeObserver(it)
            }
        }

        binding.rvDayTask.visibility = View.VISIBLE
        binding.emptyTask.visibility = View.GONE

        dayTaskObserver = Observer<List<Task>> { tasks ->
            if (tasks.isNotEmpty()) {
                fadeIn(binding.rvDayTask)
                fadeOut(binding.emptyTask)
            } else {
                fadeOut(binding.rvDayTask)
                fadeIn(binding.emptyTask)
            }
            dayTaskRecyclerAdapter.submitList(tasks)
        }

        when (selectedItem) {
            "Today" -> homeViewModel.getAllTaskToday()
                .observe(viewLifecycleOwner, dayTaskObserver!!)

            "Tomorrow" -> homeViewModel.getAllTaskTomorrow()
                .observe(viewLifecycleOwner, dayTaskObserver!!)

            "Week" -> homeViewModel.getAllTaskWeek().observe(viewLifecycleOwner, dayTaskObserver!!)
            "Month" -> homeViewModel.getAllTaskMonth()
                .observe(viewLifecycleOwner, dayTaskObserver!!)
        }
    }

    private fun loadDataProgress(selectedItem: String) {
        progressTaskObserver?.let {
            when (selectedItem) {
                "On Progress" -> homeViewModel.getAllUncompletedTasks().removeObserver(it)
                "Completed" -> homeViewModel.getAllCompletedTasks().removeObserver(it)
            }
        }

        binding.rvDayProgress.visibility = View.VISIBLE
        binding.emptyTaskProgress.visibility = View.GONE

        progressTaskObserver = Observer<List<Task>> { tasks ->
            if (tasks.isNotEmpty()) {
                progressTaskAdapter.submitList(tasks) // Cập nhật danh sách
                fadeIn(binding.rvDayProgress)
                fadeOut(binding.emptyTaskProgress)
            } else {
                fadeOut(binding.rvDayProgress)
                fadeIn(binding.emptyTaskProgress)
            }
            progressTaskAdapter.submitList(tasks)
        }

        when (selectedItem) {
            "On Progress" -> homeViewModel.getAllUncompletedTasks()
                .observe(viewLifecycleOwner, progressTaskObserver!!)

            "Completed" -> homeViewModel.getAllCompletedTasks()
                .observe(viewLifecycleOwner, progressTaskObserver!!)
        }
    }

    private fun fadeIn(view: View) {
        view.visibility = View.VISIBLE
        view.alpha = 0f
        view.animate().alpha(1f).setDuration(200).start()
    }

    private fun fadeOut(view: View) {
        view.animate().alpha(0f).setDuration(200).withEndAction {
            view.visibility = View.GONE
        }.start()
    }

    override fun onResume() {
        super.onResume()
        setupAdapters()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        dayTaskObserver = null
        progressTaskObserver = null
    }
}


