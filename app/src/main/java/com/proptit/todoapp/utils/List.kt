package com.proptit.todoapp.utils

import androidx.fragment.app.Fragment
import com.proptit.todoapp.ui.HomeFragment
import com.proptit.todoapp.item.ItemPriority


object List{
    val fragmentList = arrayListOf<Fragment>(
        HomeFragment()
    )
    val priorityItems = listOf(
        ItemPriority(1),
        ItemPriority(2),
        ItemPriority(3),
        ItemPriority(4),
        ItemPriority(5),
        ItemPriority(6),
        ItemPriority(7),
        ItemPriority(8),
        ItemPriority(9),
        ItemPriority(10),
    )

}