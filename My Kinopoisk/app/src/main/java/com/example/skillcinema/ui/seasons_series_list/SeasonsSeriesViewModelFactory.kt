package com.example.skillcinema.ui.seasons_series_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import jakarta.inject.Inject

class SeasonsSeriesViewModelFactory @Inject constructor(
    private val seriesViewModel: SeasonsSeriesViewModel
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SeasonsSeriesViewModel::class.java)){
            return seriesViewModel as T
        }
        throw IllegalArgumentException()
    }
}