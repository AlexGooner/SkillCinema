package com.citrus.skillcinema.data.database

import androidx.room.Embedded
import androidx.room.Relation

data class FilmsCollection(
    @Embedded
    val savedCollection: SavedCollection,
    @Relation(
        entity = SavedFilm::class,
        parentColumn = "collection_name",
        entityColumn = "collection_name"
    )
    val collectionFilms: List<NewSavedFilm>?
)