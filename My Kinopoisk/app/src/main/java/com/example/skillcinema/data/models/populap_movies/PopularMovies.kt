package com.example.skillcinema.data.models.populap_movies

import com.example.skillcinema.data.models.premiers.Country
import com.example.skillcinema.data.models.premiers.Genre
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PopularMovies(
    val kinopoiskId: Int,
    val nameRu: String?,
    val nameEn: String?,
    val nameOriginal: String?,
    val posterUrlPreview: String,
    val countries: List<Country>,
    val genres: List<Genre>,
    val ratingKinopoisk: Double?,
)