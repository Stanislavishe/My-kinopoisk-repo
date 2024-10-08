package com.example.skillcinema.data

import com.example.skillcinema.data.models.ImagesList
import com.example.skillcinema.data.models.SingleFilm
import com.example.skillcinema.data.models.dynamic_selections.DynamicSelectionsAndSeriesList
import com.example.skillcinema.data.models.populap_movies.PopularMoviesList
import com.example.skillcinema.data.models.premiers.PremierMoviesList
import com.example.skillcinema.data.models.search.CountryGenre
import com.example.skillcinema.data.models.search.SearchModel
import com.example.skillcinema.data.models.seasonsList.SeasonsList
import com.example.skillcinema.data.models.simularsList.SimilarList
import com.example.skillcinema.data.models.staffs.SingleStaff
import com.example.skillcinema.data.models.staffs.Staff
import com.example.skillcinema.data.models.top250.CollectionMoviesList
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface KinopoiskApi {
    @Headers("X-API-KEY: $API_KEY3")
    @GET("/api/v2.2/films/premieres")
    suspend fun premiers(
        @Query("year") year: Int,
        @Query("month") month: String
    ): PremierMoviesList

    @Headers("X-API-KEY: $API_KEY3")
    @GET("/api/v2.2/films/collections?type=TOP_250_TV_SHOWS")
    suspend fun top250List(@Query("page") page: Int): CollectionMoviesList

    @Headers("X-API-KEY: $API_KEY3")
    @GET("/api/v2.2/films/collections?type=TOP_POPULAR_MOVIES")
    suspend fun popularMoviesList(@Query("page") page: Int): PopularMoviesList

    @Headers("X-API-KEY: $API_KEY3")
    @GET("/api/v2.2/films?order=RATING&type=TV_SERIES&ratingFrom=7&ratingTo=10&yearFrom=2023&yearTo=2024")
    suspend fun popularSeries(
        @Query("page") page: Int
    ): DynamicSelectionsAndSeriesList

    @Headers("X-API-KEY: $API_KEY3")
    @GET("/api/v2.2/films/{id}")
    suspend fun findFilmOnId(@Path("id") id: String): SingleFilm

    @Headers("X-API-KEY: $API_KEY3")
    @GET("/api/v2.2/films/{id}/seasons")
    suspend fun seasonsList(@Path("id") id: String): SeasonsList

    @Headers("X-API-KEY: $API_KEY3")
    @GET("/api/v2.2/films/{id}/similars")
    suspend fun similarList(@Path("id") id: String): SimilarList

    @Headers("X-API-KEY: $API_KEY3")
    @GET("/api/v2.2/films/{id}/images")
    suspend fun imagesList(
        @Path("id") id: String,
        @Query("type") type: String,
        @Query("page") page: Int
    ): ImagesList

    @Headers("X-API-KEY: $API_KEY3")
    @GET("/api/v1/staff")
    suspend fun staffsList(@Query("filmId") id: Int): List<Staff>

    @Headers("X-API-KEY: $API_KEY3")
    @GET("/api/v1/staff/{id}")
    suspend fun singleStaff(@Path("id") id: Int): SingleStaff

    @Headers("X-API-KEY: $API_KEY3")
    @GET("/api/v2.2/films")
    suspend fun search(
        @Query("countries") countries: List<Int>,
        @Query("genres") genres: List<Int>,
        @Query("order") order: String,
        @Query("type") type: String,
        @Query("ratingFrom") ratingFrom: Int,
        @Query("ratingTo") ratingTo: Int,
        @Query("yearFrom") yearFrom: Int,
        @Query("yearTo") yearTo: Int,
        @Query("keyword") keyword: String,
        @Query("page") page: Int
    ): SearchModel

    @Headers("X-API-KEY: $API_KEY3")
    @GET("/api/v2.2/films/filters")
    suspend fun getCountryGenre(): CountryGenre

    companion object {
        private const val API_KEY = "eaa7347e-cf17-41d9-80ae-fd0cc61df1ca"
        private const val API_KEY2 = "67b11d70-4662-418d-a710-ca7099919fc4"
        private const val API_KEY3 = "3cf3f42e-06ee-4fbb-bdc7-b2e7c06bd4f4"
        private const val API_KEY4 = "b49eff68-2bd4-4386-b9b5-ed9525bff730"
    }
}

val retrofit = Retrofit.Builder()
    .baseUrl("https://kinopoiskapiunofficial.tech")
    .addConverterFactory(MoshiConverterFactory.create())
    .build()
    .create(KinopoiskApi::class.java)