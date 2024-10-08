package com.example.skillcinema.ui.hello_screens

import android.content.Context
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.skillcinema.R
import com.example.skillcinema.ui.actor_fillscreen.navigateSave
import kotlinx.coroutines.delay

class StartFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if(onBoarding()){
            Handler(activity?.mainLooper!!).postDelayed({
                findNavController().navigateSave(R.id.action_startFragment_to_homeFragment)
            }, 2000)
        } else {
            Handler(activity?.mainLooper!!).postDelayed({
                findNavController().navigateSave(R.id.action_startFragment_to_helloScreensViewPagerFragment)
            }, 2000)
        }
    }

    private fun onBoarding(): Boolean {
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("isFinished", false)
    }
}