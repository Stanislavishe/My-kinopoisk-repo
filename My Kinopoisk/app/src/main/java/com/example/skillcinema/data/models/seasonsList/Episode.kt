package com.example.skillcinema.data.models.seasonsList

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Episode(
    val seasonNumber: Int,
    val episodeNumber: Int,
    val nameRu: String?,
    val nameEn: String?,
    val releaseDate: String?
)
