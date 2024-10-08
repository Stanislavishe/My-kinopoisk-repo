package com.example.skillcinema.data.models.staffs

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Staff(
    val staffId: Int,
    val nameRu: String?,
    val nameEn: String?,
    val description: String?,
    val posterUrl: String,
    val professionKey: String,
    val professionText: String
)