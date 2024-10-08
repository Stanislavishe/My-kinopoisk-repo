package com.example.skillcinema.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "interested")
data class InterestedMovies(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "url")
    val url: String?,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "genre")
    val genre: String,
    @ColumnInfo(name = "rating")
    val rating: Double?
)