package com.example.skillcinema.data.sources

import android.annotation.SuppressLint
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.skillcinema.data.KinopoiskRepository
import com.example.skillcinema.data.models.dynamic_selections.DynamicSelectionsAndSeries
import com.example.skillcinema.use_case.FilmsUseCase

class PopularSeriesSource(
    private val filmsUseCase: FilmsUseCase
): PagingSource <Int, DynamicSelectionsAndSeries>() {
    override fun getRefreshKey(state: PagingState<Int, DynamicSelectionsAndSeries>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DynamicSelectionsAndSeries> {
        val page = params.key ?: FIRST_PAGE
        try {
        val series = filmsUseCase.getPopularSeries(page)
            return LoadResult.Page(
                data = series,
                prevKey = null,
                nextKey = if(series.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}