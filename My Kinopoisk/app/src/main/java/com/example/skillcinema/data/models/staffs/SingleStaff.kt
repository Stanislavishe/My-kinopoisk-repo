package com.example.skillcinema.data.models.staffs

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SingleStaff(
    val personId: Int,
    val nameRu: String?,
    val nameEn: String?,
    val posterUrl: String,
    val profession: String?,
    val films: List<Filmography>
)

@JsonClass(generateAdapter = true)
data class Filmography(
    val filmId: Int,
    val nameRu: String?,
    val nameEn: String?,
    val rating: String?,
    val professionKey: String
)
