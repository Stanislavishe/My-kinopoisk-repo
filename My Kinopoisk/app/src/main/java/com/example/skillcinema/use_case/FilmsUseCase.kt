package com.example.skillcinema.use_case

import android.icu.util.Calendar
import com.example.skillcinema.data.KinopoiskRepository
import com.example.skillcinema.data.models.premiers.Movie
import jakarta.inject.Inject

class FilmsUseCase @Inject constructor(
    private val kinopoiskRepository: KinopoiskRepository
) {

    suspend fun getPremiers(): List<Movie> {
        val calendar = Calendar.getInstance()
        val month = when (calendar.get(Calendar.MONTH)) {
            0 -> "JANUARY"
            1 -> "FEBRUARY"
            2 -> "MARCH"
            3 -> "APRIL"
            4 -> "MAY"
            5 -> "JUNE"
            6 -> "JULY"
            7 -> "AUGUST"
            8 -> "SEPTEMBER"
            9 -> "OCTOBER"
            10 -> "NOVEMBER"
            11 -> "DECEMBER"
            else -> ""
        }
        val year = calendar.get(Calendar.YEAR)
        return kinopoiskRepository.getPremiers(year, month)
    }

    suspend fun getPopularsFilms(page: Int) =
        kinopoiskRepository.getPopularFilms(page)

    suspend fun getTop250(page: Int) = kinopoiskRepository.getTop250(page)

    suspend fun getPopularSeries(page: Int) = kinopoiskRepository.getPopularSeries(page)

    suspend fun getItemFilm(id: Int) = kinopoiskRepository.getItemFilm(id.toString())

    suspend fun getStaffsList(id: Int) = kinopoiskRepository.getStaffsList(id)

    suspend fun getImagesList(id: Int, type: String, page: Int) =
        kinopoiskRepository.getImagesList(id.toString(), type, page).items

    suspend fun getSimilarFilms(id: Int) = kinopoiskRepository.getSimilarFilms(id.toString()).items

    suspend fun getSeasons(id: Int) = kinopoiskRepository.getSeasons(id).items

    suspend fun getSingleStaff(id: Int) = kinopoiskRepository.getSingleStaff(id)

    suspend fun getCountryGenre() = kinopoiskRepository.getCountryGenre()

//    suspend fun search(
//        countries: List<Int>,
//        genres: List<Int>,
//        order: String,
//        type: String,
//        ratingFrom: Int,
//        ratingTo: Int,
//        yearFrom: Int,
//        yearTo: Int,
//        keyword: String,
//        page: Int
//    ) = kinopoiskRepository.search(countries, genres, order, type, ratingFrom, ratingTo, yearFrom, yearTo, keyword, page).items
}