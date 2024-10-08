package com.example.skillcinema.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.skillcinema.data.models.search.CountryId
import com.example.skillcinema.data.models.search.GenreId
import com.example.skillcinema.data.models.search.SearchResult
import com.example.skillcinema.data.sources.SearchSource
import com.example.skillcinema.entity.MovieDao
import com.example.skillcinema.use_case.FilmsUseCase
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel @Inject constructor(
    private val filmsUseCase: FilmsUseCase,
    private val movieDao: MovieDao
): ViewModel() {
    private val _countryList = MutableStateFlow<List<CountryId>>(emptyList())
    val countryList = _countryList.asStateFlow()

    private val _genreList = MutableStateFlow<List<GenreId>>(emptyList())
    val genreList = _genreList.asStateFlow()

    private val _yearFrom = MutableStateFlow(YEAR_FROM_DEFAULT)
    val yearFrom = _yearFrom.asStateFlow()

    private val _yearTo = MutableStateFlow(YEAR_TO_DEFAULT)
    val yearTo = _yearTo.asStateFlow()

    private val _ratingFrom = MutableStateFlow(RATING_FROM_DEFAULT)
    val ratingFrom = _ratingFrom.asStateFlow()

    private val _ratingTo = MutableStateFlow(RATING_TO_DEFAULT)
    val ratingTo = _ratingTo.asStateFlow()

    private val _genre = MutableStateFlow(GENRE_DEFAULT)
    val genre = _genre.asStateFlow()

    private val _country = MutableStateFlow(COUNTRY_DEFAULT)
    val country = _country.asStateFlow()

    private val _order = MutableStateFlow(ORDER_DEFAULT)
    val order = _order.asStateFlow()

    private val _type = MutableStateFlow(TYPE_DEFAULT)
    val type = _type.asStateFlow()

    private val _showViewed = MutableStateFlow(true)
    val showViewed = _showViewed.asStateFlow()

    private val _viewedFlow = MutableStateFlow(emptyList<Int>())
    val viewedFlow = _viewedFlow.asStateFlow()

    fun applySettings(
        type: String, ratingFrom: Int, ratingTo: Int, yearFrom: Int, yearTo: Int,
        order: String, country: List<Int>, genre: List<Int>, viewedShow: Boolean){
        viewModelScope.launch { _yearFrom.value = yearFrom }
        viewModelScope.launch { _yearTo.value = yearTo }
        viewModelScope.launch { _order.value = order }
        viewModelScope.launch { _type.value = type }
        viewModelScope.launch { _country.value = country }
        viewModelScope.launch { _genre.value = genre }
        viewModelScope.launch { _ratingTo.value = ratingTo }
        viewModelScope.launch { _ratingFrom.value = ratingFrom }
        viewModelScope.launch { _showViewed.value = viewedShow }
        searchMovies(null)
    }

    fun searchMovies(text: CharSequence?): Flow<PagingData<SearchResult>> {
        val keyword = text ?: TEXT
        return Pager(
            config = PagingConfig(pageSize = 10),
            initialKey = null,
            pagingSourceFactory = { SearchSource(
                countries = country.value,
                genres = genre.value,
                yearFrom = yearFrom.value,
                yearTo = yearTo.value,
                ratingFrom = ratingFrom.value,
                ratingTo = ratingTo.value,
                order = order.value,
                keyword = keyword.toString(),
                type = type.value
            ) }
        ).flow.cachedIn(viewModelScope)
    }

    fun loadCountries(){
        viewModelScope.launch {
            _countryList.value = filmsUseCase.getCountryGenre().countries
        }
    }

    fun loadGenres(){
        viewModelScope.launch {
            _genreList.value = filmsUseCase.getCountryGenre().genres
        }
    }

    fun getViewed() {
        viewModelScope.launch(Dispatchers.IO) {
            val movies = movieDao.getViewedMovies()
            _viewedFlow.value = movies.map { it.id }
        }
    }

    companion object {
        const val TEXT = ""
        const val YEAR_FROM_DEFAULT = 1000
        const val YEAR_TO_DEFAULT = 3000
        val COUNTRY_DEFAULT = emptyList<Int>()
        val GENRE_DEFAULT = emptyList<Int>()
        const val RATING_FROM_DEFAULT = 0
        const val RATING_TO_DEFAULT = 10
        const val TYPE_DEFAULT = "ALL"
        const val ORDER_DEFAULT = "RATING"
    }
}