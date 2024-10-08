package com.example.skillcinema.ui.actor_fillscreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skillcinema.data.models.SingleFilm
import com.example.skillcinema.data.models.staffs.SingleStaff
import com.example.skillcinema.use_case.FilmsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ActorFullscreenViewModel @Inject constructor(
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
                val singleStaff = filmsUseCase.getSingleStaff(id)
                _staff.value = listOf(singleStaff)
                _isLoading.value = false
            } catch (e:Exception){
                Log.d("ActorFullscreenViewModel", e.toString())
                _isLoading.value = false
            }

        }
    }

    fun refresh(staffId: Int) {
        loadStaff(staffId)
    }

}