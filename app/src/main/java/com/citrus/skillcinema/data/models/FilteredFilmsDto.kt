package com.citrus.skillcinema.data.models

data class FilteredFilmsDto(
    val total: Int,
    val totalPages: Int,
    val items: List<FilteredFilmsList>
)

data class FilteredFilmsList(
    val kinopoiskId: Int,
    val imdbId: String?,
    val nameRu: String?,
    val nameEn: String?,
    val nameOriginal: String?,
    val countries: List<Country>,
    val genres: List<Genre>,
    val ratingKinopoisk: Double?,
    val ratingImdb: Double?,
    val year: Int?,
    val type: String,
    val posterUrl: String,
    val posterUrlPreview: String
)
