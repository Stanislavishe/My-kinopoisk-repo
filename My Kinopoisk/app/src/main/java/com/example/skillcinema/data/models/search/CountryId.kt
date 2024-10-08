package com.example.skillcinema.data.models.search

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CountryId(
    override val id: Int,
    val country: String
): Ids(id, country)
