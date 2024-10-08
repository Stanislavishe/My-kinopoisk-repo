package com.example.skillcinema.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import jakarta.inject.Inject

class HomeViewModelFactory @Inject constructor(
    private val homeViewModel: HomeViewModel
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)){
            return homeViewModel as T
        }
        throw IllegalArgumentException()
    }
}