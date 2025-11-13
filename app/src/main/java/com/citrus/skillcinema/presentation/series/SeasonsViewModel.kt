package com.citrus.skillcinema.presentation.series

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.citrus.skillcinema.data.MovieRepository
import com.citrus.skillcinema.data.models.Movie
import com.citrus.skillcinema.data.models.SeasonsInfoDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SeasonsViewModel @Inject constructor(private val repository: MovieRepository) :
    ViewModel() {

    private val _seasonsInfo = MutableStateFlow<SeasonsInfoDto?>(null)
    val seasonsInfo = _seasonsInfo.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _movie = MutableStateFlow<Movie?>(null)
    val movie: StateFlow<Movie?> get() = _movie

   fun loadInfo(movieId : Int){
       viewModelScope.launch(Dispatchers.IO){
           _isLoading.value = true
           runCatching {
               repository.getSeasonsInfo(movieId)
           }.fold(
               onSuccess = {
                   _seasonsInfo.value = it
               },
               onFailure = {
                   Log.d("SeasonVM", it.message ?: "Series loading fail")
               }
           )
           _isLoading.value = false
       }
   }

    fun loadMovieInfo(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true

            runCatching {
                val movie = repository.getFullInformationFilm(movieId)
                Log.d("VIEWMODEL", "MovieID - $movieId")
                _movie.value = movie
            }
            _isLoading.value = false

        }
    }

}