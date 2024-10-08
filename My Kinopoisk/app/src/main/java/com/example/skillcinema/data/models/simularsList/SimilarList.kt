package com.example.skillcinema.data.models.simularsList

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SimilarList(
    val total: Int,
    val items: List<SimilarFilm>
)
