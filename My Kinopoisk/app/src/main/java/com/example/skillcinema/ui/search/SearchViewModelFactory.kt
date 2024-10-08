package com.example.skillcinema.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import jakarta.inject.Inject

class SearchViewModelFactory @Inject constructor(
    private val searchViewModel: SearchViewModel
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)){
            return searchViewModel as T
        }
        throw IllegalArgumentException()
    }
}