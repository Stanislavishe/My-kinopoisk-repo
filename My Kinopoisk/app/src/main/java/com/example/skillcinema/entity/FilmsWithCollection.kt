package com.example.skillcinema.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class FilmsWithCollection(
    @Embedded
    val collection: Collection,
    @Relation(
        parentColumn = "name",
        entityColumn = "id",
        entity = InterestedMovies::class,
        associateBy = Junction(
            MovieFromCollection::class,
            parentColumn = "name",
            entityColumn = "id"
        )
    )
    val movie: List<InterestedMovies>
)