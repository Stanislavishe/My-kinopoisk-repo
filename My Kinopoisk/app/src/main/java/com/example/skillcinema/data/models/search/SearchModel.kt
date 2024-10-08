package com.example.skillcinema.data.models.search

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchModel(
    val total: Int,
    val totalPages: Int,
    val items: List<SearchResult>
)
