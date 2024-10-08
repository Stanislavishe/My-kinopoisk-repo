package com.example.skillcinema.ui.seasons_series_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skillcinema.data.models.seasonsList.Season
import com.example.skillcinema.use_case.FilmsUseCase
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SeasonsSeriesViewModel @Inject constructor(
    private val filmsUseCase: FilmsUseCase
) : ViewModel() {
    private val _seasonsFlow = MutableStateFlow<List<Season>>(emptyList())
    val seasonsFlow = _seasonsFlow.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun loadSeasons(id: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _seasonsFlow.value = filmsUseCase.getSeasons(id)
            } catch (e: Exception) {
                Log.d("SeasonsSeriesViewModel", "loadSeasons: $e")
            } finally {
                _isLoading.value = false
            }
        }
    }
}