package com.citrus.skillcinema.presentation.actorfulllist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.citrus.skillcinema.data.MovieRepository
import com.citrus.skillcinema.data.models.Stuff
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActorFullListViewModel @Inject constructor(private val repository: MovieRepository) :
    ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _stuffList = MutableStateFlow<List<Stuff>>(emptyList())
    val stuffList : StateFlow<List<Stuff>> = _stuffList.asStateFlow()


    fun loadList(movieId : Int) {
        viewModelScope.launch(Dispatchers.IO){
            _isLoading.value = true
            runCatching {
                val stuffList = repository.getStuff(movieId)
                _stuffList.value = stuffList
            }
            _isLoading.value = false
        }
    }
}