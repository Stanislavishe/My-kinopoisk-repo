package com.example.skillcinema.ui.hello_screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.skillcinema.R
import com.example.skillcinema.databinding.FragmentHelloScreensViewPagerBinding

class HelloScreensViewPagerFragment : Fragment() {
    private var _binding: FragmentHelloScreensViewPagerBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHelloScreensViewPagerBinding.inflate(inflater,container,false)
        val helloScreensAdapter = HelloScreensAdapter(
            listOf(FirstFragment(), SecondFragment(), ThirdFragment(), LoadingFragment()),
            parentFragmentManager, lifecycle
        )
        binding.viewPager.adapter = helloScreensAdapter
        return binding.root
    }

}