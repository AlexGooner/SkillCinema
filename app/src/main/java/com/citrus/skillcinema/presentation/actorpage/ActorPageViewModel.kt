package com.citrus.skillcinema.presentation.actorpage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.citrus.skillcinema.data.MovieRepository
import com.citrus.skillcinema.data.models.Actor
import com.citrus.skillcinema.data.models.ActorFilm
import com.citrus.skillcinema.data.models.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActorPageViewModel @Inject constructor(private val repository: MovieRepository) :
    ViewModel() {


    private val _actorInfo = MutableStateFlow<Actor?>(null)
    val actorInfo = _actorInfo.asStateFlow()

    private val _topMovies = MutableStateFlow<List<Movie>>(emptyList())
    val topMovies: StateFlow<List<Movie>> get() = _topMovies

    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> get() = _movies

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()


    fun loadInfo(personId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            runCatching {
                val actor = repository.getActor(personId)
                Log.d("VIEWMODEL", "actor - $actor")
                _actorInfo.value = actor
            }
            _isLoading.value = false
        }
    }

    fun loadMoviesByActorFilms(actorFilms : List<ActorFilm>) {
        viewModelScope.launch(Dispatchers.IO){
            _isLoading.value = true
            val filmIds = actorFilms.map { it.filmId }
            val moviesList = repository.getMoviesByIds(filmIds)
            val topRatedMovies = moviesList
                .filter { it.ratingKinopoisk != null }
                .sortedByDescending { it.ratingKinopoisk }
                .distinctBy { it.ratingKinopoisk }
                .take(10)
            _topMovies.value = topRatedMovies
            _movies.value = moviesList
            _isLoading.value = false
        }
    }
}