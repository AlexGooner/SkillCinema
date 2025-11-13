package com.citrus.skillcinema.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface SavedCollectionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(savedCollection: SavedCollection)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(savedCollections: List<SavedCollection>)
}