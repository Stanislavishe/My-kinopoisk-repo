package com.example.skillcinema.entity

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        MovieFromCollection::class,
        Collection::class,
        InterestedMovies::class,
        MoviesViewed::class,
        FilmOrSerial::class
    ],
    version = 1
)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}