package com.example.skillcinema.entity

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow


@Dao
interface MovieDao {

    @Transaction
    @Query("SELECT * FROM collections WHERE name LIKE :collection")
    fun getMoviesByType(collection: String): List<FilmsWithCollection>

    @Insert(entity = FilmOrSerial::class)
    fun insertMovie(movie: FilmOrSerial)

    @Insert(entity = MovieFromCollection::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertMovieToCollection(movieFromCollection: MovieFromCollection)

    @Delete(entity = MovieFromCollection::class)
    fun deleteMovieByType(movieFromCollection: MovieFromCollection)

    @Query("SELECT * FROM viewed")
    fun getViewedMovies(): List<MoviesViewed>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertViewedMovie(movie: MoviesViewed)

    @Delete
    fun deleteViewedMovie(movie: MoviesViewed)

    @Query("SELECT * FROM interested")
    fun getInterestedMovies(): List<InterestedMovies>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertInterestedMovie(movie: InterestedMovies)

    @Delete
    fun deleteInterestedMovie(movie: InterestedMovies)

    @Query("SELECT * FROM collections")
    fun getCollections(): Flow<List<Collection>>

    @Insert
    fun insertCollection(collection: Collection)

    @Delete
    fun deleteCollection(collection: Collection)
}