package com.example.skillcinema.ui.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skillcinema.entity.Collection
import com.example.skillcinema.entity.FilmOrSerial
import com.example.skillcinema.entity.InterestedMovies
import com.example.skillcinema.entity.MovieDao
import com.example.skillcinema.entity.MovieFromCollection
import com.example.skillcinema.entity.MoviesViewed
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel @Inject constructor(
    private val movieDao: MovieDao
) : ViewModel() {

    private val _viewedFlow = MutableStateFlow(emptyList<MoviesViewed>())
    val viewedFlow = _viewedFlow.asStateFlow()

    private val _interestedFlow = MutableStateFlow(emptyList<InterestedMovies>())
    val interestedFlow = _interestedFlow.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    var collections = movieDao.getCollections()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    private val _addInCollection = Channel<String>()
    val addInCollection = _addInCollection.receiveAsFlow()

    private val _deleteOnCollection = Channel<String>()
    val deleteOnCollection = _deleteOnCollection.receiveAsFlow()

    private val _isError = Channel<Boolean>()
    val isError = _isError.receiveAsFlow()

    fun getViewed() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isLoading.value = true
                _viewedFlow.value = movieDao.getViewedMovies()
            } catch (e: Exception) {
                Log.d("ProfileViewModel", "getViewed: $e")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun refresh() {
        collections = movieDao.getCollections()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )
        getViewed()
        getInterested()
    }

    fun addInCollection(name: String) {
        viewModelScope.launch {
            _addInCollection.send(name)
        }
    }

    fun deleteOnCollection(name: String) {
        viewModelScope.launch {
            _deleteOnCollection.send(name)
        }
    }

    fun getInterested() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isLoading.value = true
                _interestedFlow.value = movieDao.getInterestedMovies()
            } catch (e: Exception) {
                Log.d("ProfileViewModel", "getInterested: $e")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun setCollection(collection: Collection) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                movieDao.insertCollection(collection)
                _isError.send(false)
            } catch (_: Exception) {
                _isError.send(true)
            }
        }
    }

    fun setFilmForCollection(movie: FilmOrSerial, name: String) {
        viewModelScope.launch(Dispatchers.IO) {

            movieDao.insertMovieToCollection(MovieFromCollection(name, movie.id))

        }
    }

    fun deleteFilmFromCollection(movie: FilmOrSerial, name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            movieDao.deleteMovieByType(MovieFromCollection(name, movie.id))
        }
    }

}