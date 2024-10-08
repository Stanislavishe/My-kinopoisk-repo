package com.example.skillcinema.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.skillcinema.data.models.populap_movies.PopularMovies
import com.example.skillcinema.data.models.premiers.Movie
import com.example.skillcinema.data.models.simularsList.SimilarFilm
import com.example.skillcinema.data.sources.PopularFilmsSource
import com.example.skillcinema.data.sources.PopularSeriesSource
import com.example.skillcinema.data.sources.Top250Source
import com.example.skillcinema.entity.FilmOrSerial
import com.example.skillcinema.entity.FilmsWithCollection
import com.example.skillcinema.entity.InterestedMovies
import com.example.skillcinema.entity.MovieDao
import com.example.skillcinema.entity.MovieFromCollection
import com.example.skillcinema.entity.MoviesViewed
import com.example.skillcinema.use_case.FilmsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel @Inject constructor(
    private val filmsUseCase: FilmsUseCase,
    private val movieDao: MovieDao
) : ViewModel() {

    private val _premiers = MutableStateFlow<List<Movie>>(emptyList())
    val premiers = _premiers.asStateFlow()

    private val _similarFilms = MutableStateFlow<List<SimilarFilm>>(emptyList())
    val similarFilm = _similarFilms.asStateFlow()

    val popularsMovies: Flow<PagingData<PopularMovies>> = Pager(
        config = PagingConfig(
            pageSize = 10
        ),
        initialKey = null,
        pagingSourceFactory = { PopularFilmsSource(filmsUseCase) }
    ).flow.cachedIn(viewModelScope)

    val top250: Flow<PagingData<PopularMovies>> = Pager(
        PagingConfig(pageSize = 10),
        initialKey = null,
        pagingSourceFactory = { Top250Source(filmsUseCase) }
    ).flow.cachedIn(viewModelScope)

    val serials = Pager(
        PagingConfig(pageSize = 10),
        initialKey = null,
        pagingSourceFactory = { PopularSeriesSource(filmsUseCase) }
    ).flow.cachedIn(viewModelScope)

    private val _viewedFlow = MutableStateFlow(emptyList<Int>())
    val viewedFlow = _viewedFlow.asStateFlow()

    private val _viewedListFlow = MutableStateFlow(emptyList<MoviesViewed>())
    val viewedListFlow = _viewedListFlow.asStateFlow()

    private val _interestedFlow = MutableStateFlow(emptyList<InterestedMovies>())
    val interestedFlow = _interestedFlow.asStateFlow()

    private val _movieCollection = MutableStateFlow<List<FilmsWithCollection>>(emptyList())
    val movieCollection = _movieCollection.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _isError = MutableStateFlow(false)
    val isError = _isError.asStateFlow()

    fun loadPremiers() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isLoading.value = true
                _premiers.value = filmsUseCase.getPremiers()
            } catch (e: Exception) {
                _isError.value = true
                Log.d("HomeViewModel", "loadPremiers: $e")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadSimilarFilms(id: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _similarFilms.value = filmsUseCase.getSimilarFilms(id)
            } catch (e: Exception){
                _isError.value = true
                Log.d("HomeViewModel", e.toString())
            } finally {
                _isLoading.value = false
            }

        }
    }

    fun getViewed() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isLoading.value = true
                val movies = movieDao.getViewedMovies()
                _viewedFlow.value = movies.map { it.id }
            } catch (e: Exception) {
                Log.d("HomeViewModel", e.toString())
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getInterested() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isLoading.value = true
                _interestedFlow.value = movieDao.getInterestedMovies()
            } catch (e: Exception) {
                Log.d("HomeViewModel", e.toString())
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getMoviesCollection(name: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isLoading.value = true
                _movieCollection.value = movieDao.getMoviesByType(name)
            } catch (e: Exception) {
                Log.d("HomeViewModel", e.toString())
            } finally {
                _isLoading.value = false
            }
        }
    }
}