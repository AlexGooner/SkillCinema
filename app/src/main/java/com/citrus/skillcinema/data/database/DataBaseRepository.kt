package com.citrus.skillcinema.data.database

import javax.inject.Inject


class DataBaseRepository  @Inject constructor(private val savedFilmDao: SavedFilmDao) {

    fun getCollection() = savedFilmDao.getCollection()

    suspend fun insertFilm(film : NewSavedFilm) = savedFilmDao.insertFilm(film)

    suspend fun insertCollection(savedCollection: SavedCollection) = savedFilmDao.insertCollection(savedCollection)

    suspend fun deleteFilm(collectionName: String, filmId: Int) = savedFilmDao.deleteFilm(collectionName, filmId)

    suspend fun deleteAllFilms(collectionName : String) = savedFilmDao.deleteAllFilms(collectionName)

    suspend fun deleteCollection(collectionName: String) = savedFilmDao.deleteCollection(collectionName)

    suspend fun getInterestedFilms(collectionName: String) = savedFilmDao.getInterested(collectionName)

    suspend fun deleteAllInterestedFilms(collectionName: String) = savedFilmDao.deleteAllInterestedFilms(collectionName)



    // Методы для работы с просмотренными фильмами
    suspend fun insertWatchedFilm(watchedFilm: WatchedFilm) = savedFilmDao.insertWatchedFilm(watchedFilm)

    suspend fun getWatchedFilm() = savedFilmDao.getWatchedFilm()

    suspend fun updateWatchedStatus(filmId: Int, isWatched: Boolean) = savedFilmDao.updateWatchedStatus(filmId, isWatched)

    suspend fun deleteWatchedFilm(filmId: Int) = savedFilmDao.deleteWatchedFilm(filmId)

    suspend fun deleteAllWatchedFilms() = savedFilmDao.deleteAllWatchedFilms()


}