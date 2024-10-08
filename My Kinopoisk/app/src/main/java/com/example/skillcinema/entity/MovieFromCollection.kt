package com.example.skillcinema.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "collection_movies", primaryKeys = ["name", "id"])
data class MovieFromCollection(
    @ColumnInfo(name = "name")
    val collectionName: String,
    @ColumnInfo(name ="id")
    val movieId: Int
)