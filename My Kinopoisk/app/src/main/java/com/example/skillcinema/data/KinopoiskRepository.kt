package com.example.skillcinema.data

import com.example.skillcinema.data.models.staffs.SingleStaff
import jakarta.inject.Inject

class KinopoiskRepository @Inject constructor() {
    suspend fun getPremiers(year: Int, month: String) =
        retrofit.premiers(year, month).items

    suspend fun getPopularFilms(page: Int) =
        retrofit.popularMoviesList(page).items

    suspend fun getTop250(page: Int) =
        retrofit.top250List(page).items

    suspend fun getPopularSeries(page: Int) =
        retrofit.popularSeries(page).items

    suspend fun getItemFilm(id: String) =
        retrofit.findFilmOnId(id)

    suspend fun getStaffsList(id: Int) =
        retrofit.staffsList(id)

    suspend fun getImagesList(id: String, type: String, page: Int) =
        retrofit.imagesList(id, type, page)

    suspend fun getSimilarFilms(id: String) = retrofit.similarList(id)

    suspend fun getSeasons (id: Int) = retrofit.seasonsList(id.toString())

    suspend fun getSingleStaff(id: Int): SingleStaff? {
        try {
            val staff = retrofit.singleStaff(id)
            return staff
        } catch (e: Exception) {
            return null
        }
    }

    suspend fun getCountryGenre() = retrofit.getCountryGenre()

    suspend fun search(
        countries: List<Int>,
        genres: List<Int>,
        order: String,
        type: String,
        ratingFrom: Int,
        ratingTo: Int,
        yearFrom: Int,
        yearTo: Int,
        keyword: String,
        page: Int
    ) = retrofit.search(countries, genres, order, type, ratingFrom, ratingTo, yearFrom, yearTo, keyword, page)
}