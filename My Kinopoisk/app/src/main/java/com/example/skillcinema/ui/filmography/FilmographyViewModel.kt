package com.example.skillcinema.ui.filmography

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skillcinema.data.models.staffs.SingleStaff
import com.example.skillcinema.use_case.FilmsUseCase
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FilmographyViewModel @Inject constructor(
    val filmsUseCase: FilmsUseCase
): ViewModel() {

    private val _staff = MutableStateFlow<List<SingleStaff?>>(emptyList())
    val staff = _staff.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun loadStaff(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isLoading.value = true
                _staff.value = listOf(filmsUseCase.getSingleStaff(id))
            } catch (e: Exception) {
                Log.d("FilmographyViewModel", "loadStaff: $e", )
            } finally {
                _isLoading.value = false
            }
        }
    }
}