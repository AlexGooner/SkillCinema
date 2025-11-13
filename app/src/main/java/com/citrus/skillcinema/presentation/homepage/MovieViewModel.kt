package com.citrus.skillcinema.presentation.homepage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.citrus.skillcinema.data.MovieRepository
import com.citrus.skillcinema.data.database.DataBaseRepository
import com.citrus.skillcinema.data.models.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random
import kotlin.random.nextInt

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repository: MovieRepository, private val dataBaseRepository: DataBaseRepository
) : ViewModel() {


    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _premierMovies = MutableStateFlow<List<Movie>>(emptyList())
    private val _topMovies = MutableStateFlow<List<Movie>>(emptyList())
    private val _popularMovies = MutableStateFlow<List<Movie>>(emptyList())
    private val _series = MutableStateFlow<List<Movie>>(emptyList())
    private val _randomCAndG = MutableStateFlow<List<Movie>>(emptyList())


    val premierMovies: StateFlow<List<Movie>> = _premierMovies.asStateFlow()
    val topMovies: StateFlow<List<Movie>> = _topMovies.asStateFlow()
    val popularMovies: StateFlow<List<Movie>> = _popularMovies.asStateFlow()
    val series: StateFlow<List<Movie>> = _series.asStateFlow()
    val randomCAndG : StateFlow<List<Movie>> = _randomCAndG.asStateFlow()

    private val _currentCountry = MutableStateFlow(0)
    val currentCountry: StateFlow<Int> = _currentCountry.asStateFlow()

    private val _currentGenre = MutableStateFlow(0)
    val currentGenre: StateFlow<Int> = _currentGenre.asStateFlow()

    init {
        loadPremieres()
        loadTops()
        loadPopular()
        loadSeries()
        loadRandomCountryAndGenre()
    }

    private fun loadPremieres() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            runCatching {
                repository.getPremieres(2025, "MARCH")
            }.fold(
                onSuccess = { _premierMovies.value = it!! },
                onFailure = { Log.d("MovieListViewModel", it.message ?: "ПРОБЛЕМА ТУТ") }
            )
            _isLoading.value = false
        }
    }

    private fun loadTops() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            runCatching {
                repository.getCollections("TOP_250_MOVIES", 1)
            }.fold(
                onSuccess = { _topMovies.value = it!! },
                onFailure = { Log.d("MovieTopViewModel", it.message ?: "") }
            )
            _isLoading.value = false
        }
    }

    private fun loadPopular() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            runCatching {
                repository.getCollections("TOP_POPULAR_MOVIES", 1)
            }.fold(
                onSuccess = { _popularMovies.value = it!! },
                onFailure = { Log.d("MoviePopular", it.message ?: "") }
            )
            _isLoading.value = false
        }
    }

    private fun loadSeries() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            runCatching {
                repository.getCollections("TOP_250_TV_SHOWS", 1)
            }.fold(
                onSuccess = { _series.value = it!! },
                onFailure = { Log.d("Series", it.message ?: "") }
            )
            _isLoading.value = false
        }
    }

    private fun loadRandomCountryAndGenre() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            runCatching {
                val country = Random.Default.nextInt(1..3)
                val genre = Random.Default.nextInt(1..3)
                repository.getRandomCountryAndGenres(country, genre, 1).also {
                    _currentCountry.value = country
                    _currentGenre.value = genre
                }
            }.fold(
                onSuccess = {_randomCAndG.value = it!!},
                onFailure = { Log.d("Random", it.message ?: "")}
            )
            _isLoading.value = false
        }
    }

    suspend fun getWatchedFilmIds(): List<Int> {
        val watchedFilms = dataBaseRepository.getWatchedFilm()
        return watchedFilms.map { it.filmId }
    }

}