package com.example.skillcinema.data.models.search

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GenreId (
    override val id: Int,
    val genre: String
): Ids(id, genre)