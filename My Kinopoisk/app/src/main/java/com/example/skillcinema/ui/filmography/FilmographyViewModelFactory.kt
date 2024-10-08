package com.example.skillcinema.ui.filmography

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import jakarta.inject.Inject

class FilmographyViewModelFactory @Inject constructor(
    private val filmographyViewModel: FilmographyViewModel
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(FilmographyViewModel::class.java)){
            return filmographyViewModel as T
        }
        throw IllegalArgumentException()
    }
}