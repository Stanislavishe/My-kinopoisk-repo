package com.example.skillcinema.data.models.simularsList

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SimilarFilm(
    val filmId: Int,
    val nameRu: String?,
    val nameEn: String?,
    val nameOriginal: String?,
    val posterUrlPreview: String
)
