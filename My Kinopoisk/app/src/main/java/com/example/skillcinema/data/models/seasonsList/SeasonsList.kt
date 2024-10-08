package com.example.skillcinema.data.models.seasonsList

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SeasonsList(
    val total: Int,
    val items: List<Season>
)

@JsonClass(generateAdapter = true)
data class Season(
    val number: Int,
    val episodes: List<Episode>
)