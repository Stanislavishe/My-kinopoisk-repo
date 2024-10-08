package com.example.skillcinema.ui.film_fullscreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.skillcinema.data.models.SingleFilm
import com.example.skillcinema.data.models.simularsList.SimilarFilm
import com.example.skillcinema.data.models.staffs.Staff
import com.example.skillcinema.data.sources.ImageListSource
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
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class FullscreenViewModel @Inject constructor(
    private val filmsUseCase: FilmsUseCase,
    private val movieDao: MovieDao
) : ViewModel() {

    private val _movie = MutableStateFlow<List<SingleFilm>>(emptyList())
    val movie = _movie.asStateFlow()

    private val staffsList = MutableStateFlow<List<Staff>>(emptyList())

    private val _staffsMap = MutableStateFlow(emptyMap<String, List<Staff>>())
    val staffsMap = _staffsMap.asStateFlow()

    private val _similarFilms = MutableStateFlow<List<SimilarFilm>>(emptyList())
    val similarFilm = _similarFilms.asStateFlow()

    private val _viewedFlow = MutableStateFlow(emptyList<Int>())
    val viewedFlow = _viewedFlow.asStateFlow()

    private val _movieCollection = MutableStateFlow<List<FilmsWithCollection>>(emptyList())
    val movieCollection = _movieCollection.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun filmFrames(id: Int) = Pager(
        config = PagingConfig(pageSize = 10),
        initialKey = null,
        pagingSourceFactory = { ImageListSource(filmsUseCase, id, "STILL") }
    ).flow.cachedIn(viewModelScope)

    private fun getActors() {
        val staffsList = staffsList.value
        val actors = staffsList.filter { it.professionKey == ACTOR }
        Log.d("FullscreenViewModel", "getActors: $actors")
        val other = staffsList.filter { it.professionKey != ACTOR }
        Log.d("FullscreenViewModel", "getActors: $other")
        _staffsMap.value = mapOf(Pair(ACTOR, actors), Pair(OTHER, other))
    }

    fun setViewed(movie: MoviesViewed){
        viewModelScope.launch(Dispatchers.IO) {
            movieDao.insertViewedMovie(movie)
        }
    }

    fun setInterested(movie: InterestedMovies){
        viewModelScope.launch(Dispatchers.IO) {
            movieDao.insertInterestedMovie(movie)
        }
    }
    fun getMoviesCollection(name: String){
        viewModelScope.launch(Dispatchers.IO) {
            _movieCollection.value = movieDao.getMoviesByType(name)
        }
    }

    fun setFilmForCollection(movieId: Int, name: String){
        viewModelScope.launch(Dispatchers.IO) {
            movieDao.insertMovieToCollection(MovieFromCollection(name, movieId))
        }
    }



    fun deleteFilmInCollection(movieId: Int, name: String){
        viewModelScope.launch(Dispatchers.IO) {
            movieDao.deleteMovieByType(MovieFromCollection(name, movieId))
        }
    }

    fun deleteViewed(movie: MoviesViewed){
        viewModelScope.launch(Dispatchers.IO) {
            movieDao.deleteViewedMovie(movie)
        }
    }

    fun loadSimilarFilms(id: Int){
        viewModelScope.launch {
            _similarFilms.value = filmsUseCase.getSimilarFilms(id)
        }
    }

    fun loadStaffs(id: Int){
        viewModelScope.launch {
            staffsList.value = filmsUseCase.getStaffsList(id)
            getActors()
        }
    }

    fun loadMovie(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isLoading.value = true
                val movie = filmsUseCase.getItemFilm(id)
                _movie.value = listOf(movie)
            }catch (e: Exception){
                Log.d("FullscreenViewModel",e.toString())
            } finally {
                _isLoading.value = false
            }

        }
    }

    fun getViewed() {
        viewModelScope.launch(Dispatchers.IO) {
            _viewedFlow.value = movieDao.getViewedMovies().map { it.id }
        }
    }

    fun refresh(filmId: Int) {
        loadMovie(filmId)
    }

    companion object {
        const val ACTOR = "ACTOR"
        const val OTHER = "OTHER"
        const val SIMILAR = "SIMILAR"
    }
}
