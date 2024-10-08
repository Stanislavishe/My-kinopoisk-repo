package com.example.skillcinema.data.sources

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.skillcinema.data.KinopoiskRepository
import com.example.skillcinema.data.models.populap_movies.PopularMovies
import com.example.skillcinema.use_case.FilmsUseCase

class PopularFilmsSource(
    private val filmsUseCase: FilmsUseCase
) : PagingSource<Int, PopularMovies>() {


    override fun getRefreshKey(state: PagingState<Int, PopularMovies>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PopularMovies> {
        val page = params.key ?: FIRST_PAGE
        try {
            val films = filmsUseCase.getPopularsFilms(page)

            return LoadResult.Page(
                data = films,
                prevKey = null,
                nextKey = if (films.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            Log.d("PopularFilmsSource", "load: $exception")
            return LoadResult.Error(exception)
        }
    }
}