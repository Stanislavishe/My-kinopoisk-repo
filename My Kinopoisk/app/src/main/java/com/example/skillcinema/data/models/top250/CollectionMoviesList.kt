package com.example.skillcinema.data.models.top250


import com.example.skillcinema.data.models.populap_movies.PopularMovies
import com.example.skillcinema.data.models.premiers.Movie
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CollectionMoviesList(
    val total: Int,
    val totalPages: Int,
    val items: List<PopularMovies>
)