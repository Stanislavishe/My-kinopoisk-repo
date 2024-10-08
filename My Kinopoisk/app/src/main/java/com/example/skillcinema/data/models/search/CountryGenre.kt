package com.example.skillcinema.data.models.search

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CountryGenre(
    val genres: List<GenreId>,
    val countries: List<CountryId>
)
