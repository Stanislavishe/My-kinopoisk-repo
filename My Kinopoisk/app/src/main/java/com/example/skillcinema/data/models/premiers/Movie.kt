package com.example.skillcinema.data.models.premiers

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
data class Movie(
    val kinopoiskId: Int,
    val nameRu: String,
    val year: Int,
    val posterUrlPreview: String,
    val countries: List<Country>,
    val genres: List<Genre>
)

@Parcelize
@JsonClass(generateAdapter = true)
data class Country(
    val country: String
): Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class Genre(
    val genre: String
): Parcelable