package com.example.skillcinema.data.sources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.skillcinema.data.models.Image
import com.example.skillcinema.use_case.FilmsUseCase

const val FIRST_PAGE = 1

class ImageListSource(
    private val filmsUseCase: FilmsUseCase,
    private val id: Int,
    private val type: String
): PagingSource<Int, Image>() {
    override fun getRefreshKey(state: PagingState<Int, Image>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Image> {
        val page = params.key ?: FIRST_PAGE
        try {
            val images = filmsUseCase.getImagesList(id, type, page)

            return LoadResult.Page(
                data = images,
                prevKey = null,
                nextKey = if (images.isEmpty()) null else page + 1
            )
        } catch (e: Exception){
            return LoadResult.Error(e)
        }
    }
}