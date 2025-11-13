package com.citrus.skillcinema.data.models

data class MovieList(
    val items: List<Movie>?,
    val total: Int?,
    val category: String?,
    val filterCategory: Int?,
    val movies: List<Movie>?
)


