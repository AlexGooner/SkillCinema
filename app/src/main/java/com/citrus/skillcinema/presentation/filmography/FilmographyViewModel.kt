package com.citrus.skillcinema.presentation.filmography

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.citrus.skillcinema.data.MovieRepository
import com.citrus.skillcinema.data.models.Actor
import com.citrus.skillcinema.data.models.ActorFilm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilmographyViewModel @Inject constructor(private val repository: MovieRepository) :
    ViewModel() {

    private val _actorInfo = MutableStateFlow<Actor?>(null)
    val actorInfo = _actorInfo.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _asWriterFilms = MutableStateFlow<List<ActorFilm>>(emptyList())
    val asWriterFilms = _asWriterFilms.asStateFlow()

    private val _asDirectorFilms = MutableStateFlow<List<ActorFilm>>(emptyList())
    val asDirectorFilms = _asDirectorFilms.asStateFlow()

    private val _asProducerFilms = MutableStateFlow<List<ActorFilm>>(emptyList())
    val asProducerFilms = _asProducerFilms.asStateFlow()

    private val _asActorFilms = MutableStateFlow<List<ActorFilm>>(emptyList())
    val asActorFilms = _asActorFilms.asStateFlow()

    private val _asHimselfFilms = MutableStateFlow<List<ActorFilm>>(emptyList())
    val asHimselfFilms = _asHimselfFilms.asStateFlow()

    private val _otherFilms = MutableStateFlow<List<ActorFilm>>(emptyList())
    val otherFilms = _otherFilms.asStateFlow()


    private val _isItMan = MutableStateFlow<Boolean>(true)
    val isItMan  = _isItMan.asStateFlow()




    fun loadInfo(personId: Int){
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            runCatching {
                repository.getActor(personId)
            }.fold(
                onSuccess = {info ->
                    _actorInfo.value = info
                    _isItMan.value = if (info.sex == "FEMALE") false else true
                    val listOfFilms = info.films.toMutableList()

                    val writer = mutableListOf<ActorFilm>()
                    val director = mutableListOf<ActorFilm>()
                    val producer =  mutableListOf<ActorFilm>()
                    val actor = mutableListOf<ActorFilm>()
                    val himself = mutableListOf<ActorFilm>()
                    val other = mutableListOf<ActorFilm>()
                    listOfFilms.onEach { film ->
                        when(film.professionKey) {
                            "WRITER" -> writer.add(film)
                            "DIRECTOR" -> director.add(film)
                            "PRODUCER" -> producer.add(film)
                            "ACTOR" -> actor.add(film)
                            "HIMSELF" -> himself.add(film)
                            else -> other.add(film)
                        }
                    }
                    _asWriterFilms.value = writer
                    _asDirectorFilms.value = director
                    _asProducerFilms.value = producer
                    _asActorFilms.value = actor
                    _asHimselfFilms.value = himself
                    _otherFilms.value = other
                },
                onFailure = {
                    Log.d("FILMOGRAPHY_VM", it.message ?: "Getting film list unsuccessful")
                }
            )
            _isLoading.value = false
        }
    }
}