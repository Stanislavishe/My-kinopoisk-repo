package com.example.skillcinema.ui.actor_fillscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.skillcinema.ui.film_fullscreen.FullscreenViewModel
import jakarta.inject.Inject

class ActorFullscreenViewModelFactory  @Inject constructor(
    private val actorFullscreenViewModel: ActorFullscreenViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ActorFullscreenViewModel::class.java)) {
            return actorFullscreenViewModel as T
        }
        throw IllegalArgumentException()
    }
}