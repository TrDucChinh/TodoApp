package com.proptit.todoapp.utils

import androidx.fragment.app.Fragment
import com.proptit.todoapp.R
import com.proptit.todoapp.item.ItemColor
import com.proptit.todoapp.item.ItemIcon
import com.proptit.todoapp.ui.HomeFragment
import com.proptit.todoapp.item.ItemPriority
import com.proptit.todoapp.model.Category
import com.proptit.todoapp.ui.CalendarFragment


object ListData {
    val fragmentList = arrayListOf<Fragment>(
        HomeFragment(),
        CalendarFragment()
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
    val categoryItems = listOf(
        Category(titleCategory = "Work", idColor = R.color.cyan, idIcon = R.drawable.category_add),
    )
    val colorCategoryItems = listOf(
        ItemColor(R.color.cyan),
        ItemColor(R.color.green),
        ItemColor(R.color.orange),
        ItemColor(R.color.pink),
        ItemColor(R.color.red),
        ItemColor(R.color.yellow),
        ItemColor(R.color.blue),
        ItemColor(R.color.gray_blue),
        ItemColor(R.color.light_purple),
        ItemColor(R.color.light_yellow),
        ItemColor(R.color.light_green),
        ItemColor(R.color.light_sea_blue),
        ItemColor(R.color.sea_blue),
        ItemColor(R.color.violet),
    )
    val iconCategoryItems = listOf(
        ItemIcon(R.drawable.category_bag),
        ItemIcon(R.drawable.category_music),
        ItemIcon(R.drawable.category_bread),
        ItemIcon(R.drawable.category_home),
        ItemIcon(R.drawable.category_math),
        ItemIcon(R.drawable.category_school),
        ItemIcon(R.drawable.category_megaphone),
        ItemIcon(R.drawable.category_heart_beat),
        ItemIcon(R.drawable.category_video_camera),
        ItemIcon(R.drawable.category_dumbbell),
    )
    val dayDropDownItem = listOf(
        "Today",
        "Tomorrow",
        "Week",
        "Month",
    )
    val progressionDropDownItem = listOf(
        "On Progress",
        "Completed",
    )

}