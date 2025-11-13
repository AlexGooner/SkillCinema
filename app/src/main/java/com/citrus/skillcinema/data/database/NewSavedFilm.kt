package com.citrus.skillcinema.data.database

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class NewSavedFilm(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int? = null,
    @ColumnInfo(name = "collection_name")
    val collectionName: String,
    @ColumnInfo(name = "film_id")
    val filmId: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "genres")
    val genres: String,
    @ColumnInfo(name = "rating")
    val rating: Double?,
    @ColumnInfo(name = "url")
    val url: String
)
