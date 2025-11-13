package com.citrus.skillcinema.data

import com.citrus.skillcinema.data.models.Actor
import com.citrus.skillcinema.data.models.FilteredFilmsList
import com.citrus.skillcinema.data.models.GalleryItem
import com.citrus.skillcinema.data.models.Movie
import com.citrus.skillcinema.data.models.SeasonsInfoDto
import com.citrus.skillcinema.data.models.Stuff
import com.citrus.skillcinema.data.retrofit.retrofit

class MovieRepository {
    suspend fun getPremieres(year: Int, month: String): List<Movie>? {
        return retrofit.getPremieres(year, month).items
    }

    suspend fun getCollections(type: String, page: Int): List<Movie>? {
        return retrofit.getCollectionFilms(type, page).items
    }

    suspend fun getRandomCountryAndGenres(country: Int, genre: Int, page: Int): List<Movie>? {
        return retrofit.getRandomGenreAndCountry(country, genre, page).items
    }

    suspend fun getFullInformationFilm(id: Int): Movie {
        return retrofit.getFullInformationFilm(id)
    }

    suspend fun getStuff(id: Int): List<Stuff> {
        val stuff = retrofit.getStuff(id)
        return stuff
    }

    suspend fun getImages(id: Int, type: String, page: Int): List<GalleryItem> {
        val images = retrofit.getImages(id, type, page).items
        return images
    }

    suspend fun getSimilar(id: Int): List<Movie>? {
        return retrofit.getSimilarMovies(id).items
    }

    suspend fun getSeasonsInfo(id: Int): SeasonsInfoDto {
        val seasons = retrofit.getSeasonsInfo(id)
        return seasons
    }

    suspend fun getActor(id: Int): Actor {
        val actor = retrofit.getActorInfo(id)
        return actor
    }

    suspend fun getMovieById(filmId: Int): Movie {
        return retrofit.getFilmById(filmId)
    }

    suspend fun getMoviesByIds(filmsIds: List<Int>): List<Movie> {
        return filmsIds.map { getMovieById(it) }
    }

    suspend fun getSearch(
        countries: List<Int>, genres: List<Int>, type: String, order: String, ratingFrom: Int,
        ratingTo: Int, yearFrom: Int, yearTo: Int, keyword: String, page: Int
    ): List<FilteredFilmsList> {
        return retrofit.getSearch(
            countries, genres, type, order, ratingFrom, ratingTo, yearFrom, yearTo, keyword, page
        ).items
    }

}