package com.proptit.todoapp.dialogfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.proptit.todoapp.adapter.recyclerviewadapter.PriorityRecyclerAdapter
import com.proptit.todoapp.databinding.FragmentPriorityPickerBinding
import com.proptit.todoapp.interfaces.IPriorityListener


class PriorityPickerFragment(
    private val priorityListener: IPriorityListener,
    private val selectedPriority: Int, // Lưu vị trí đã chọn
) : DialogFragment() {

    private var _binding: FragmentPriorityPickerBinding? = null
    private val binding get() = _binding!!
    private val priorityAdapter: PriorityRecyclerAdapter by lazy {
        PriorityRecyclerAdapter(priorityListener, selectedPriority)
    }

    companion object {
        const val TAG = "PriorityFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPriorityPickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            priorityRecyclerview.layoutManager = GridLayoutManager(context, 4)
            priorityRecyclerview.adapter = priorityAdapter

            saveBtn.setOnClickListener {
                dismiss()
            }
            cancelBtn.setOnClickListener {
                // Không thay đổi priority
                priorityListener.onClickPriority(selectedPriority)
                dismiss()
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}