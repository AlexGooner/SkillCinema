package com.citrus.skillcinema.data.models


data class Movie(
    val kinopoiskId: Int,
    val nameRu: String?,
    val nameOriginal: String?,
    val posterUrl: String?,
    val posterUrlPreview: String?,
    val coverUrl: String?,
    val logoUrl: String?,
    val countries: List<Country>?,
    val genres: List<Genre>?,
    val year: Int?,
    val filmId: Int,
    val ratingKinopoisk: Double?,
    val premiereRu: String,
    var isWatched: Boolean,
    val filmLength: Int,
    val ratingAgeLimits: String?,
    val shortDescription: String?,
    val description: String?,
    val serial: Boolean
)

data class Country(
    val country: String?
)

data class Genre(
    val genre: String?
)