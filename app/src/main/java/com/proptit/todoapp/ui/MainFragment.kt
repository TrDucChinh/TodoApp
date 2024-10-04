package com.proptit.todoapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.proptit.todoapp.adapter.viewpageradapter.FragmentMainViewPager
import com.proptit.todoapp.databinding.FragmentMainBinding
import com.proptit.todoapp.dialogfragment.AddTaskFragment
import com.proptit.todoapp.utils.ListData
import me.ibrahimsn.lib.SmoothBottomBar

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private lateinit var pagerAdapter: FragmentMainViewPager
    private lateinit var viewPager: ViewPager2
    private lateinit var smoothBottomBar: SmoothBottomBar

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMainBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponent()
        binding.fab.setOnClickListener {
            onBtnAddTaskClick()
        }
    }

    private fun initComponent() {
        viewPager = binding.viewPager
        smoothBottomBar = binding.bottomNavigationViewLinear
        pagerAdapter = FragmentMainViewPager(
            childFragmentManager,
            lifecycle,
            ListData.fragmentList
        )
        viewPager.adapter = pagerAdapter
    }

    private fun initBehavior() {

    }

    private fun onBtnAddTaskClick() {
        AddTaskFragment().show(parentFragmentManager, AddTaskFragment.TAG)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}