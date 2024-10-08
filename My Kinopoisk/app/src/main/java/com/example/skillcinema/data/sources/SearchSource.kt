package com.example.skillcinema.data.sources

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.skillcinema.data.KinopoiskRepository
import com.example.skillcinema.data.models.search.SearchResult

class SearchSource(
    private val countries: List<Int>,
    private val genres: List<Int>,
    private val order: String,
    private val type: String,
    private val ratingFrom: Int,
    private val ratingTo: Int,
    private val yearFrom: Int,
    private val yearTo: Int,
    private val keyword: String,
): PagingSource<Int, SearchResult>() {

    private val repository = KinopoiskRepository()

    override fun getRefreshKey(state: PagingState<Int, SearchResult>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchResult> {
        val page = params.key ?: FIRST_PAGE
        try {
            val films = repository.search(countries, genres, order, type, ratingFrom, ratingTo, yearFrom, yearTo, keyword, page).items
            Log.d("PagingSource", "load: $films")
            return LoadResult.Page(
                data = films,
                prevKey = null,
                nextKey = if (films.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}