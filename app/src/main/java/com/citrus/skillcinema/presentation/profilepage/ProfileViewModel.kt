package com.citrus.skillcinema.presentation.profilepage

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.citrus.skillcinema.data.MovieRepository
import com.citrus.skillcinema.data.database.DataBaseRepository
import com.citrus.skillcinema.data.database.DefaultCollectionsName
import com.citrus.skillcinema.data.database.SavedCollection
import com.citrus.skillcinema.data.models.Movie
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
class ProfileViewModel @Inject constructor(
    private val dataBaseRepository: DataBaseRepository,
    private val repository: MovieRepository
) : ViewModel() {


    private val _watchedMovies = MutableStateFlow<List<Movie>>(emptyList())
    val watchedMovies: StateFlow<List<Movie>> get() = _watchedMovies


    private val _likedMovies = MutableStateFlow<List<Movie>>(emptyList())
    val likedMovies: StateFlow<List<Movie>> get() = _likedMovies

    private val _interestedMovies = MutableStateFlow<List<Movie>>(emptyList())
    val interestedMovies: StateFlow<List<Movie>> get() = _interestedMovies



    val collectionsName = DefaultCollectionsName()

    val collections = this.dataBaseRepository.getCollection().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(5000L),
        initialValue = emptyList()
    )


    fun deleteCollection(collectionName: String) {
        viewModelScope.launch {
            collections.value.forEach { filmsCollection ->
                if (filmsCollection.savedCollection.collectionName == collectionName) {
                    dataBaseRepository.deleteAllFilms(collectionName)
                    dataBaseRepository.deleteCollection(collectionName)

                }
            }
        }
    }

    private val _collectionAdded = MutableStateFlow("")
    val collectionAdded = _collectionAdded.asStateFlow()


    fun loadWatchedFilms() {
        viewModelScope.launch(Dispatchers.IO) {
            val watchedFilms = dataBaseRepository.getWatchedFilm()
            val moviesList = watchedFilms.map { watchedFilm ->
                repository.getMovieById(watchedFilm.filmId)
            }
            _watchedMovies.value = moviesList
        }
    }

    fun getInterestedFilms(){
        viewModelScope.launch(Dispatchers.IO) {
            val interestedFilms = dataBaseRepository.getInterestedFilms(collectionsName.interested)
            val moviesList = interestedFilms.map { films ->
                repository.getMovieById(films.filmId)
            }
            _interestedMovies.value = moviesList
        }
    }

    fun loadDefaultCollections(){
        viewModelScope.launch(Dispatchers.IO) {
            dataBaseRepository.insertCollection(savedCollection = SavedCollection("Любимые"))
            dataBaseRepository.insertCollection(savedCollection = SavedCollection("Хочу посмотреть"))
        }
    }


    fun deleteWatchedFilms(){
        viewModelScope.launch(Dispatchers.IO) {
            dataBaseRepository.deleteAllWatchedFilms()
            _watchedMovies.value = emptyList()
        }
    }

    fun deleteInterestedFilms(){
        viewModelScope.launch(Dispatchers.IO) {
            dataBaseRepository.deleteAllInterestedFilms(collectionsName.interested)
            _interestedMovies.value = emptyList()
        }
    }

    fun addCollection(collectionName: String) {
        viewModelScope.launch {
            try {
                dataBaseRepository.insertCollection(SavedCollection(collectionName))
            } catch (e: SQLiteConstraintException) {
                _collectionAdded.value = collectionName
            }
        }
    }
}