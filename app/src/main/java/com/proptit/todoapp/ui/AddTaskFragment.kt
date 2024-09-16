package com.proptit.todoapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.proptit.todoapp.databinding.FragmentAddTaskBinding
import com.proptit.todoapp.interfaces.IAddTaskListener

class AddTaskFragment() : BottomSheetDialogFragment() {
    private var _binding: FragmentAddTaskBinding? = null
    private val binding get() = _binding!!
    companion object{
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
    private fun initBehavior(){
        binding.apply {
            btnSetTime.setOnClickListener {
            }
            btnSetPriority.setOnClickListener {
            }
            btnSetCategory.setOnClickListener{

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}