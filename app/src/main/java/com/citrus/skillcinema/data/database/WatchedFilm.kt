package com.citrus.skillcinema.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watched_films")
data class WatchedFilm(
    @PrimaryKey
    @ColumnInfo(name = "film_id")
    val filmId: Int,
    @ColumnInfo(name = "is_watched")
    val isWatched: Boolean = false
)