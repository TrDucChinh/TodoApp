package com.proptit.todoapp.dialogfragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.proptit.todoapp.adapter.recyclerviewadapter.IconCategoryRecyclerAdapter
import com.proptit.todoapp.databinding.FragmentChoseIconCategoryBinding
import com.proptit.todoapp.interfaces.IIconCategoryListener


class ChoseIconCategory(
    private val iconCategoryListener: IIconCategoryListener,
    private val selectedIconCategory: Int, // Lưu vị trí đã chọn
) : DialogFragment() {
    private var _binding: FragmentChoseIconCategoryBinding? = null
    private val binding get() = _binding!!
    private var checkSaveIcon = 0
    private val iconCategoryAdapter: IconCategoryRecyclerAdapter by lazy {
        IconCategoryRecyclerAdapter(iconCategoryListener, selectedIconCategory)
    }
    companion object {
        const val TAG = "ChoseIconCategory"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentChoseIconCategoryBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponent()
    }

    private fun initComponent() {
        binding.apply {
            iconCategoryRecyclerview.adapter = iconCategoryAdapter
            iconCategoryRecyclerview.layoutManager = GridLayoutManager(context, 3)
            btnSave.setOnClickListener {
                checkSaveIcon = 1  // Đặt giá trị checkSaveIcon
                // Gửi kết quả về CreateCategoryFragment
                val result = Bundle().apply {
                    putInt("checkSaveIcon", checkSaveIcon)
                }
                parentFragmentManager.setFragmentResult("requestKey", result)
                dismiss()
            }

            btnCancel.setOnClickListener {
                iconCategoryListener.onIconClick(selectedIconCategory)
                dismiss()
            }
        }
    }
    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.95).toInt(),  // Width is 95% of the screen
//            (resources.displayMetrics.heightPixels * 0.5).toInt()   // Height is 80% of the screen
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}