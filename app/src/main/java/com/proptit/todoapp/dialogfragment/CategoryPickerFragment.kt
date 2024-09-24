package com.proptit.todoapp.dialogfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.proptit.todoapp.R
import com.proptit.todoapp.adapter.recyclerviewadapter.CategoryRecyclerAdapter
import com.proptit.todoapp.databinding.FragmentCategoryPickerBinding
import com.proptit.todoapp.interfaces.ICategoryListener
import com.proptit.todoapp.model.Category
import com.proptit.todoapp.viewmodel.CategoryViewModel

class CategoryPickerFragment(
    private val categoryListener: ICategoryListener,
    /*private val selectedCategory: Int,*/
) : DialogFragment() {
    private var _binding: FragmentCategoryPickerBinding? = null
    private val binding get() = _binding!!
    private val categoryAdapter: CategoryRecyclerAdapter by lazy {
        CategoryRecyclerAdapter(categoryListener /*selectedCategory*/)
    }

    private val categoryViewModel : CategoryViewModel by activityViewModels {
        CategoryViewModel.CategoryViewModelFactory(requireActivity().application)
    }

    companion object {
        const val TAG = "CategoryPickerFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCategoryPickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponent()
    }

    private fun initComponent() {
        binding.categoryRecyclerview.apply {
            adapter = categoryAdapter
            layoutManager = GridLayoutManager(context, 3)
        }
        categoryViewModel.getAllCategory().observe(viewLifecycleOwner) {
            categoryAdapter.submitList(it)
        }
    }
    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.95).toInt(),  // Width is 95% of the screen
            (resources.displayMetrics.heightPixels * 0.8).toInt()   // Height is 80% of the screen
        )
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}