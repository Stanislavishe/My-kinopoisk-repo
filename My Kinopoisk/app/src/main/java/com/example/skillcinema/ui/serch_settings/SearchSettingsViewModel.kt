package com.example.skillcinema.ui.serch_settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skillcinema.data.models.search.CountryId
import com.example.skillcinema.data.models.search.GenreId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchSettingsViewModel: ViewModel() {
    private val _countryFlow = MutableStateFlow<List<CountryId>>(emptyList())
    val countryFlow = _countryFlow.asStateFlow()

    private val _genreFlow = MutableStateFlow<List<GenreId>>(emptyList())
    val genreFlow = _genreFlow.asStateFlow()

    private val _yearFlow = MutableStateFlow(listOf(1000, 3000))
    val yearFlow = _yearFlow.asStateFlow()

    fun sendCountry(country: CountryId){
        viewModelScope.launch {
            _countryFlow.value = listOf(country)
        }
    }

    fun sendGenre(genre: GenreId){
        viewModelScope.launch {
            _genreFlow.value = listOf(genre)
        }
    }

    fun sendYears(from: Int, to: Int) {
        viewModelScope.launch {
            _yearFlow.value = listOf(from, to)
        }
    }
}