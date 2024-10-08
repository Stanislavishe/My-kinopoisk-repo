package com.example.skillcinema.ui.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.skillcinema.data.sources.ImageListSource
import com.example.skillcinema.use_case.FilmsUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class GalleryViewModel @Inject constructor(
    private val filmsUseCase: FilmsUseCase
): ViewModel() {

    fun filmFrames(name: String, id: Int) = Pager(
        config = PagingConfig(pageSize = 10),
        initialKey = null,
        pagingSourceFactory = { ImageListSource(filmsUseCase, id, name) }
    ).flow.cachedIn(viewModelScope)

}