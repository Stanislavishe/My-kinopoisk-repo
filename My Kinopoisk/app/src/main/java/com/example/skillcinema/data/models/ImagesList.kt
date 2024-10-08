package com.example.skillcinema.data.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImagesList(
    val total: Int,
    val totalPages: Int,
    val items: List<Image>
)

@JsonClass(generateAdapter = true)
data class Image(
    val previewUrl: String
)
