package com.example.skillcinema.ui.serch_settings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.skillcinema.R
import com.example.skillcinema.databinding.FragmentDatePickerBinding
import com.example.skillcinema.ui.actor_fillscreen.navigateSave
import kotlinx.coroutines.launch

class DatePickerFragment : Fragment() {

    private var _binding: FragmentDatePickerBinding? = null
    private val binding get() = _binding!!

    private val searchSettingsViewModel: SearchSettingsViewModel by activityViewModels()

    private val adapterFrom = DateAdapter { onFromClick(it) }
    private val adapterTo = DateAdapter { onToClick(it) }

    private var startFrom = 60
    private var startTo = 60
    private val dateList = mutableListOf<Int>()

    private var from = 1000
    private var to = 3000


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDatePickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (1966..2025).map {
            dateList.add(it)
        }
        with(binding) {
            datesListFrom.adapter = adapterFrom
            datesListTo.adapter = adapterTo
            backToDates()
            backFromDates()
            backDateFrom.setOnClickListener {
                backFromDates()
            }
            backDateTo.setOnClickListener {
                backToDates()
            }
            nextDateFrom.setOnClickListener {
                nextFromDates()
            }
            nextDateTo.setOnClickListener {
                nextToDates()
            }
            applyBtn.setOnClickListener {
                findNavController().navigateSave(R.id.action_datePickerFragment_to_searchSettingsFragment)
                searchSettingsViewModel.sendYears(from, to)
            }
            backButton.setOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun onFromClick(date: Int) {
        from = date
    }

    private fun onToClick(date: Int) {
        to = date
    }

    private fun rangeYearsTo(){
        with(binding){
            catchDateSeasonTo.text = when (startTo) {
                0 -> "${dateList[0]}-${dateList[11]}"
                12 -> "${dateList[12]}-${dateList[23]}"
                24 -> "${dateList[24]}-${dateList[35]}"
                36 -> "${dateList[36]}-${dateList[47]}"
                else -> "${dateList[48]}-${dateList[59]}"
            }
        }
    }

    private fun rangeYearsFrom(){
        with(binding){
            catchDateSeasonFrom.text = when (startFrom) {
                0 -> "${dateList[0]}-${dateList[11]}"
                12 -> "${dateList[12]}-${dateList[23]}"
                24 -> "${dateList[24]}-${dateList[35]}"
                36 -> "${dateList[36]}-${dateList[47]}"
                else -> "${dateList[48]}-${dateList[59]}"
            }
        }
    }

    private fun nextToDates() {
        val test = startTo + 12
        if (test <= dateList.size) {
            val list = mutableListOf<Int>()
            for (i in startTo..<test) {
                list.add(dateList[i])
            }
            startTo += 12
            viewLifecycleOwner.lifecycleScope.launch {
                adapterTo.setData(list)
            }
        }
        Log.d("DatePickerFragment", "nextoDates: $startTo")
        binding.backDateTo.isEnabled = startTo >= 11
        binding.nextDateTo.isEnabled = startTo <= 48
        rangeYearsTo()
    }

    private fun nextFromDates() {
        val test = startFrom + 12
        if (test <= dateList.size) {
            val list = mutableListOf<Int>()
            for (i in startFrom..<test) {
                list.add(dateList[i])
            }
            startFrom += 12
            viewLifecycleOwner.lifecycleScope.launch {
                adapterFrom.setData(list)
            }
        }
        binding.nextDateFrom.isEnabled = startFrom <= 48
        binding.backDateFrom.isEnabled = startFrom >= 11
        rangeYearsFrom()
    }

    private fun backToDates() {
        val test = startTo - 12
        if (test >= 0) {
            val list = mutableListOf<Int>()
            for (i in test..<startTo) {
                list.add(dateList[i])
            }
            startTo -= 12
            viewLifecycleOwner.lifecycleScope.launch {
                adapterTo.setData(list)
            }
        }
        Log.d("DatePickerFragment", "backToDates: $startTo")
        binding.backDateTo.isEnabled = startTo >= 11
        binding.nextDateTo.isEnabled = startTo <= 48
        rangeYearsTo()
    }

    private fun backFromDates() {
        val test = startFrom - 12
        if (test >= 0) {
            val list = mutableListOf<Int>()
            for (i in test..<startFrom) {
                list.add(dateList[i])
            }
            startFrom -= 12
            viewLifecycleOwner.lifecycleScope.launch {
                adapterFrom.setData(list)
            }
        }
        binding.nextDateFrom.isEnabled = startFrom <= 48
        binding.backDateFrom.isEnabled = startFrom >= 11
        rangeYearsFrom()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}