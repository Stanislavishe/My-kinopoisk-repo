package com.example.skillcinema.ui.hello_screens

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class HelloScreensAdapter(
    private val list: List<Fragment>,
    fm: FragmentManager,
    lifecycle: Lifecycle
): FragmentStateAdapter(fm, lifecycle) {
    private val fragments = list

    override fun getItemCount() = list.size

    override fun createFragment(position: Int) = fragments[position]
}