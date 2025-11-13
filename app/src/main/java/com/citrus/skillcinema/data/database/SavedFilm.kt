package com.citrus.skillcinema.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "saved_films", indices = [Index(value = ["collection_name", "film_id"], unique = true)])
data class SavedFilm(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
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
