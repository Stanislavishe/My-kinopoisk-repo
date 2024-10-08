package com.example.skillcinema.ui.film_fullscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import jakarta.inject.Inject

class FullscreenViewModelFactory @Inject constructor(
    private val fullscreenViewModel: FullscreenViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FullscreenViewModel::class.java)) {
            return fullscreenViewModel as T
        }
        throw IllegalArgumentException()
    }
}