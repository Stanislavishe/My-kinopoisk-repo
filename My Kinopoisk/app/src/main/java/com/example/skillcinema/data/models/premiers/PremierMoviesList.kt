package com.example.skillcinema.data.models.premiers

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PremierMoviesList(
    val total: Int,
    val items: List<Movie>
)