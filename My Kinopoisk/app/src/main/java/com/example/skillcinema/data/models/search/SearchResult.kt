package com.example.skillcinema.data.models.search

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchResult(
    val kinopoiskId: Int,
    val nameRu: String?,
    val nameEn: String?,
    val nameOriginal: String?,
    val ratingKinopoisk: Double?,
    val year: Int?,
    val posterUrlPreview: String
)
