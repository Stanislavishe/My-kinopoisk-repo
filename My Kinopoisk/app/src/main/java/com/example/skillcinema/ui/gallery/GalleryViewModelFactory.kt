package com.example.skillcinema.ui.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import jakarta.inject.Inject

class GalleryViewModelFactory @Inject constructor(
    private val galleryViewModel: GalleryViewModel
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(GalleryViewModel::class.java)){
            return galleryViewModel as T
        }
        throw IllegalArgumentException()
    }
}