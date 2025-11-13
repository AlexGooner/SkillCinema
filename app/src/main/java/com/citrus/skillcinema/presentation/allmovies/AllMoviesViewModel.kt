package com.citrus.skillcinema.presentation.allmovies

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.citrus.skillcinema.data.MovieRepository
import com.citrus.skillcinema.data.models.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllMoviesViewModel @Inject constructor(private val repository: MovieRepository) :
    ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()


    private val _movies = MutableLiveData<List<Movie>?>()
    val movies: MutableLiveData<List<Movie>?> get() = _movies


    fun loadTopMovies(type: String, title: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true

            if (type == "TOP_250_MOVIES") {
                val movieList = repository.getCollections(type, 1)
                _movies.value = movieList ?: emptyList()
            }
        }
        _isLoading.value = false

    }

    fun loadSeries(type: String, title: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true

            if (type == "TOP_250_TV_SHOWS") {
                val movieList = repository.getCollections(type, 1)
                _movies.value = movieList ?: emptyList()
            }
        }
        _isLoading.value = false

    }

    fun loadPopular(type: String, title: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true

            if (type == "TOP_POPULAR_MOVIES") {
                val movieList = repository.getCollections(type, 1)
                _movies.value = movieList ?: emptyList()
            }
        }
        _isLoading.value = false

    }

    fun loadPremiers(year: Int, month: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true

            val movieList = repository.getPremieres(year, month)
            _movies.value = movieList ?: emptyList()
        }
        _isLoading.value = false

    }

    fun loadRandomMovies(genre: Int, country: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true

            val movies = repository.getRandomCountryAndGenres(country, genre, 1)
            _movies.postValue(movies)
        }
        _isLoading.value = false

    }


    fun loadMoreMovies(type: String, page: Int): Flow<List<Movie>> = flow {
        _isLoading.value = true

        val movies = repository.getCollections(type, page)
        emit(movies ?: emptyList())
        _isLoading.value = false
    }.flowOn(Dispatchers.IO)
}