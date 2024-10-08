package com.example.skillcinema.data.models.dynamic_selections

import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class DynamicSelectionsAndSeriesList(
    val total: Int,
    val totalPages: Int,
    val items: List<DynamicSelectionsAndSeries>
)
