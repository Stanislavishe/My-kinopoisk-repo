package com.example.skillcinema.data.models.populap_movies

import com.example.skillcinema.data.models.premiers.Movie
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PopularMoviesList(
    val total: Int,
    val totalPages: Int,
    val items: List<PopularMovies>
)