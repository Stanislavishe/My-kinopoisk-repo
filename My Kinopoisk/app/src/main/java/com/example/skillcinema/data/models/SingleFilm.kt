package com.example.skillcinema.data.models

import android.os.Parcelable
import com.example.skillcinema.data.models.premiers.Country
import com.example.skillcinema.data.models.premiers.Genre
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class SingleFilm(
    val kinopoiskId: Int,
    val nameRu: String?,
    val nameEn: String?,
    val nameOriginal: String?,
    val year: Int?,
    val coverUrl: String?,
    val logoUrl: String?,
    val posterUrlPreview: String?,
    val ratingKinopoisk: Double?,
    val filmLength: Int?,
    val description: String?,
    val type: String,
    val shortDescription: String?,
    val ratingAgeLimits: String?,
    val countries: List<Country>,
    val genres: List<Genre>
): Parcelable
