package com.citrus.skillcinema.presentation.certainmovie

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.citrus.skillcinema.data.MovieRepository
import com.citrus.skillcinema.data.database.DataBaseRepository
import com.citrus.skillcinema.data.database.DefaultCollectionsName
import com.citrus.skillcinema.data.database.NewSavedFilm
import com.citrus.skillcinema.data.database.SavedCollection
import com.citrus.skillcinema.data.database.WatchedFilm
import com.citrus.skillcinema.data.models.GalleryItem
import com.citrus.skillcinema.data.models.Movie
import com.citrus.skillcinema.data.models.SeasonsInfoDto
import com.citrus.skillcinema.data.models.Stuff
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CertainMovieViewModel @Inject constructor(
    private val repository: MovieRepository,
    private val dbRepository: DataBaseRepository
) : ViewModel() {

    val collectionName = DefaultCollectionsName()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isWatched = MutableStateFlow(false)
    val isWatched: StateFlow<Boolean> get() = _isWatched

    private val _isFilmLiked = MutableStateFlow(false)
    val isFilmLiked = _isFilmLiked.asStateFlow()

    private val _isFilmBookmark = MutableStateFlow(false)
    val isFilmBookmark = _isFilmBookmark.asStateFlow()

    private val _isInterested = MutableStateFlow(false)

    private var savedFilmLike: NewSavedFilm? = null
    private var savedFilmWatched: NewSavedFilm? = null
    private var savedFilmWantWatch: NewSavedFilm? = null
    private var savedFilmInterested: NewSavedFilm? = null
    val collectionsName = DefaultCollectionsName()
    val savedCollections = dbRepository.getCollection()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    init {
        viewModelScope.launch {
            savedCollections.collect {
                Log.d("DataBase", it.toString())
            }
        }
    }

    private val _movieCertain = MutableStateFlow<Movie?>(null)
    val movieCertain: StateFlow<Movie?> get() = _movieCertain

    private val _stuffList = MutableStateFlow<List<Stuff>>(emptyList())
    val stuffList: StateFlow<List<Stuff>> = _stuffList.asStateFlow()

    private val _imagesList = MutableStateFlow<List<GalleryItem?>>(emptyList())
    val imagesList: StateFlow<List<GalleryItem?>> get() = _imagesList

    private val _similarList = MutableStateFlow<List<Movie>>(emptyList())
    val similarList: StateFlow<List<Movie>> = _similarList.asStateFlow()

    private val _seasonsInfo = MutableStateFlow<SeasonsInfoDto?>(null)
    val seasonsInfo = _seasonsInfo.asStateFlow()


    fun toggleWatchedStatus(filmId: Int, isWatched: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            if (isWatched) {
                dbRepository.insertWatchedFilm(WatchedFilm(filmId, true))
            } else {
                dbRepository.deleteWatchedFilm(filmId)
            }
            _isWatched.value = isWatched
        }
    }




    fun loadMovieCertain(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            runCatching {
                val movie = repository.getFullInformationFilm(movieId)
                Log.d("VIEWMODEL", "MovieID - $movieId")
                _movieCertain.value = movie


                val stuffList = repository.getStuff(movieId)
                Log.d("VIEWMODEL", "stuffList - $stuffList")
                _stuffList.value = stuffList

                val similarList = repository.getSimilar(movieId)
                _similarList.value = similarList!!
            }
            _isLoading.value = false
        }
    }

    fun loadImages(movieId: Int, type: String, page: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            runCatching {
                val imagesList = repository.getImages(movieId, type, page)
                _imagesList.value = imagesList
            }
            _isLoading.value = false
        }
    }

    fun loadSeasonsInfo(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true

            runCatching {
                val seasInfo = repository.getSeasonsInfo(movieId)
                _seasonsInfo.value = seasInfo
            }
            _isLoading.value = false

        }
    }

    fun getTotalEpisodesCount(): Int {
        return _seasonsInfo.value?.items?.sumOf { season ->
            season.episodes.size
        } ?: 0
    }

    fun checkIcons(id: Int, name: String, genres: String, rating: Double?, url: String) {
        savedFilmLike = NewSavedFilm(
            collectionName = collectionsName.liked,
            filmId = id,
            name = name,
            genres = genres,
            rating = rating,
            url = url
        )
        savedFilmWatched = NewSavedFilm(
            collectionName = collectionsName.watched,
            filmId = id,
            name = name,
            genres = genres,
            rating = rating,
            url = url
        )
        savedFilmWantWatch = NewSavedFilm(
            collectionName = collectionsName.wantWatch,
            filmId = id,
            name = name,
            genres = genres,
            rating = rating,
            url = url
        )
        savedFilmInterested = NewSavedFilm(
            collectionName = collectionsName.interested,
            filmId = id,
            name = name,
            genres = genres,
            rating = rating,
            url = url
        )
        viewModelScope.launch {
            savedCollections.value.forEach { filmsCollection ->
                if (filmsCollection.savedCollection.collectionName == collectionsName.liked) {
                    _isFilmLiked.value =
                        filmsCollection.collectionFilms!!.any { it.filmId == savedFilmLike!!.filmId && it.collectionName == collectionsName.liked }
                }
            }
            savedCollections.value.forEach { filmsCollection ->
                if (filmsCollection.savedCollection.collectionName == collectionsName.watched) {
                    _isWatched.value =
                        filmsCollection.collectionFilms!!.any { it.filmId == savedFilmWatched!!.filmId && it.collectionName == collectionsName.watched }
                }
            }
            savedCollections.value.forEach { filmsCollection ->
                if (filmsCollection.savedCollection.collectionName == collectionsName.wantWatch) {
                    _isFilmBookmark.value =
                        filmsCollection.collectionFilms!!.any { it.filmId == savedFilmWantWatch!!.filmId && it.collectionName == collectionsName.wantWatch }
                }
            }
            savedCollections.value.forEach { filmsCollection ->
                if (filmsCollection.savedCollection.collectionName == collectionsName.interested) {
                    _isInterested.value =
                        filmsCollection.collectionFilms!!.any{ it.filmId == savedFilmInterested!!.filmId && it.collectionName == collectionsName.interested}
                }
            }
        }
    }

    fun onIconButtonClick(collectionName: String) {
        val savedFilm = when (collectionName) {
            collectionsName.liked -> savedFilmLike
            collectionsName.watched -> savedFilmWatched
            collectionsName.wantWatch -> savedFilmWantWatch
            collectionsName.interested -> savedFilmInterested
            else -> null
        }
        viewModelScope.launch {
            savedCollections.value.forEach { filmsCollection ->
                if (filmsCollection.savedCollection.collectionName == collectionName) {
                    if (filmsCollection.collectionFilms!!.any { it.filmId == savedFilm!!.filmId && it.collectionName == collectionName }) {
                        dbRepository.deleteFilm(collectionName, savedFilm!!.filmId)
                        when (collectionName) {
                            collectionsName.liked -> _isFilmLiked.value = false
                            collectionsName.watched -> _isWatched.value = false
                            collectionsName.wantWatch -> _isFilmBookmark.value = false
                            collectionsName.interested -> _isInterested.value = false
                        }
                    } else {
                        dbRepository.insertFilm(savedFilm!!)
                        when (collectionName) {
                            collectionsName.liked -> _isFilmLiked.value = true
                            collectionsName.watched -> _isWatched.value = true
                            collectionsName.wantWatch -> _isFilmBookmark.value = true
                            collectionsName.interested -> _isInterested.value = true
                        }
                    }
                }
            }
        }
    }



    fun checkFilm(
        collectionName: String,
        filmId: Int,
        title: String,
        genres: String,
        rating: Double?,
        url: String
    ) {
        val savedFilm = NewSavedFilm(
            collectionName = collectionName,
            filmId = filmId,
            name = title,
            genres = genres,
            rating = rating,
            url = url
        )

        viewModelScope.launch {
            val isFilmInCollection = savedCollections.value.any { filmsCollection ->
                filmsCollection.savedCollection.collectionName == collectionName &&
                        filmsCollection.collectionFilms!!.any { it.filmId == filmId }
            }

            if (isFilmInCollection) {
                dbRepository.deleteFilm(collectionName, filmId)
                when (collectionName) {
                    collectionsName.liked -> _isFilmLiked.value = false
                    collectionsName.watched -> _isWatched.value = false
                    collectionsName.wantWatch -> _isFilmBookmark.value = false
                    collectionsName.interested -> _isInterested.value = false

                }
            } else {
                dbRepository.insertFilm(savedFilm)
                when (collectionName) {
                    collectionsName.liked -> _isFilmLiked.value = true
                    collectionsName.watched -> _isWatched.value = true
                    collectionsName.wantWatch -> _isFilmBookmark.value = true
                    collectionsName.interested -> _isInterested.value = true
                }
            }
        }
    }

    private val _collectionAdded = MutableStateFlow("")
    val collectionAdded = _collectionAdded.asStateFlow()
    fun addCollection(collectionName: String) {
        viewModelScope.launch {
            try {
                dbRepository.insertCollection(SavedCollection(collectionName))
            } catch (e: SQLiteConstraintException) {
                _collectionAdded.value = collectionName
            }
        }
    }
}