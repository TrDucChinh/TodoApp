package com.proptit.todoapp.dialogfragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginLeft
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.proptit.todoapp.adapter.recyclerviewadapter.ColorCategoryRecyclerAdapter
import com.proptit.todoapp.databinding.FragmentCreateCategoryBinding
import com.proptit.todoapp.interfaces.IColorListener
import com.proptit.todoapp.interfaces.IIconCategoryListener
import com.proptit.todoapp.viewmodel.CreateCategoryViewModel


class CreateCategoryFragment : DialogFragment(

) {
    private var _binding: FragmentCreateCategoryBinding? = null
    private val binding get() = _binding!!
    private var selectedIconCategory = -1
    private var selectedColor = -1

    //    private var checkSaveIcon = 0
    private val colorCategoryAdapter: ColorCategoryRecyclerAdapter by lazy {
        ColorCategoryRecyclerAdapter(object : IColorListener {
            override fun onColorClick(color: Int) {
                selectedColor = color
            }
        })
    }
    private val createCategoryViewModel: CreateCategoryViewModel by activityViewModels {
        CreateCategoryViewModel.CreateCategoryViewModelFactory(requireActivity().application)
    }

    companion object {
        const val TAG = "CreateCategoryFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCreateCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        childFragmentManager.setFragmentResultListener("requestKey", this) { _, bundle ->
            val checkSaveIcon = bundle.getInt("checkSaveIcon", 0)
            if (checkSaveIcon == 1) {
                binding.chosenCategoryIcon.apply {
                    setCompoundDrawablesWithIntrinsicBounds(
                        selectedIconCategory,
                        0,
                        0,
                        0
                    ) // Thay icon khi save
                    text = ""
                }
            }
        }

        initComponent()


    }

    private fun initComponent() {
        binding.apply {
            rvCategoryColor.apply {
                adapter = colorCategoryAdapter
                layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }
            chosenCategoryIcon.apply {
                setOnClickListener {
                    choseIconCategory()
                }
            }

            saveBtn.setOnClickListener {
//                Log.d("CreateCategoryFragment", "iconCategory: $selectedIconCategory")
//                Log.d("CreateCategoryFragment", "iconColor: $selectedColor")
                if (selectedIconCategory == -1 || selectedColor == -1 || etCategoryName.text.toString().isEmpty()
                ) {
                    return@setOnClickListener
                } else {
                    createCategoryViewModel.insertCategory(
                        etCategoryName.text.toString(),
                        selectedColor,
                        selectedIconCategory
                    )
                    dismiss()
                }
            }
            cancelBtn.setOnClickListener {
                dismiss()
            }
        }
    }

    private fun choseIconCategory() {

        val choseIconCategory = ChoseIconCategory(object : IIconCategoryListener {
            override fun onIconClick(icon: Int) {
                icon.let {
                    selectedIconCategory = it
                }
            }
        }, selectedIconCategory)
        choseIconCategory.show(childFragmentManager, ChoseIconCategory.TAG)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}