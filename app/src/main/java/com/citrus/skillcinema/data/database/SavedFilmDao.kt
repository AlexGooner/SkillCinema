package com.citrus.skillcinema.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedFilmDao {

    @Transaction
    @Query("SELECT * FROM savedCollection")
    fun getCollection(): Flow<List<FilmsCollection>>

    @Insert(entity = SavedFilm::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFilm(film: NewSavedFilm)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCollection(savedCollection: SavedCollection)

    @Query("DELETE FROM saved_films WHERE collection_name = :collectionName AND film_id = :filmId")
    suspend fun deleteFilm(collectionName: String, filmId: Int)

    @Query("DELETE FROM saved_films WHERE collection_name = :collectionName")
    suspend fun deleteAllFilms(collectionName: String)

    @Query("DELETE FROM savedCollection WHERE collection_name = :collectionName")
    suspend fun deleteCollection(collectionName: String)

    @Query("DELETE FROM savedCollection")
    suspend fun deleteAllCollections()

    @Query("SELECT * FROM saved_films WHERE collection_name = :collectionName")
    suspend fun getInterested(collectionName: String) : List<SavedFilm>

    @Query("DELETE FROM saved_films WHERE collection_name = :collectionName")
    suspend fun deleteAllInterestedFilms(collectionName: String)


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWatchedFilm(watchedFilm: WatchedFilm)

    @Query("SELECT * FROM watched_films")
    suspend fun getWatchedFilm(): List<WatchedFilm>

    @Query("UPDATE watched_films SET is_watched = :isWatched WHERE film_id = :filmId")
    suspend fun updateWatchedStatus(filmId: Int, isWatched: Boolean)

    @Query("DELETE FROM watched_films WHERE film_id = :filmId")
    suspend fun deleteWatchedFilm(filmId: Int)

    @Query("DELETE FROM watched_films")
    suspend fun deleteAllWatchedFilms()




}