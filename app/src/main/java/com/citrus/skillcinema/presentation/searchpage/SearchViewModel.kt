package com.citrus.skillcinema.presentation.searchpage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.citrus.skillcinema.data.MovieRepository
import com.citrus.skillcinema.data.database.DataBaseRepository
import com.citrus.skillcinema.data.models.CountryAndGenreList
import com.citrus.skillcinema.data.models.FilmType
import com.citrus.skillcinema.data.models.FilmsOrder
import com.citrus.skillcinema.data.models.FilteredFilmsList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@OptIn(FlowPreview::class)
class SearchViewModel @Inject constructor(private val repository: MovieRepository, private val dataBaseRepository: DataBaseRepository) :
    ViewModel() {

    private val searchQuery = MutableStateFlow("")

    init {
        viewModelScope.launch {
            searchQuery
                .debounce(300)
                .distinctUntilChanged()
                .collect { query ->
                    if (query.isNotEmpty()) {
                        getFilms(query)
                    } else {
                        clearSearch()
                    }
                }
        }
    }

    fun setSearchQuery(query: String) {
        searchQuery.value = query
    }


    private val filmType = FilmType()
    private val filmOrder = FilmsOrder()
    private val countryAndGenresLists = CountryAndGenreList()

    private var orderFilter = filmOrder.year
    private var countryFilter = countryAndGenresLists.countryList[0]
    private var genreFilter = countryAndGenresLists.genreList[0]
    private var yearFromFilter: Int? = 1970
    private var yearToFilter: Int? = 2025
    private var ratingFromFilter = 1
    private var ratingToFilter = 10

    private var typeFilter = filmType.allTypes

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _searchedFilms = MutableStateFlow<List<FilteredFilmsList>>(emptyList())
    val searchedFilms = _searchedFilms.asStateFlow()

    private fun getFilms(text: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            delay(1000)
            runCatching {
                repository.getSearch(
                    countryFilter, genreFilter, typeFilter, orderFilter,
                    ratingFromFilter, ratingToFilter, yearFromFilter!!,
                    yearToFilter!!, text, FIRST_PAGE
                )
            }.fold(
                onSuccess = { filmList ->

                    val filteredList = filmList.filter { film ->
                        !film.nameRu.isNullOrEmpty() || !film.nameEn.isNullOrEmpty()
                    }
                    _searchedFilms.value = filteredList
                },
                onFailure = {
                    _searchedFilms.value = emptyList()
                }
            )
            _isLoading.value = false
        }
    }

     fun getSearch(
        country: List<Int>, genre: List<Int>, type: String, order: String,
        ratingFrom: Int, ratingTo: Int, yearFrom: Int, yearTo: Int, page: Int, isWatched: Boolean
    ) {
        viewModelScope.launch(Dispatchers.IO){
            _isLoading.value = true
            runCatching {
                repository.getSearch(country, genre, type, order, ratingFrom, ratingTo, yearFrom, yearTo, "", page)
            }.fold(
                onSuccess = {
                    filmList ->
                    val filteredList = filmList.filter { film ->
                        !film.nameRu.isNullOrEmpty() || !film.nameEn.isNullOrEmpty()
                    }
                    if (isWatched) {
                        val watchedFilms = dataBaseRepository.getWatchedFilm()
                        val filteredWatchedFilms = watchedFilms.map { it.filmId }

                        val finalList = filteredList.filterNot { film ->
                            filteredWatchedFilms.contains(film.kinopoiskId)
                        }
                        _searchedFilms.value = finalList
                    } else _searchedFilms.value = filteredList


                },
                onFailure = {
                    _searchedFilms.value = emptyList()
                }
            )
            _isLoading.value = false

        }
    }

    suspend fun getWatchedFilmIds(): List<Int> {
        val watchedFilms = dataBaseRepository.getWatchedFilm()
        return watchedFilms.map { it.filmId }
    }

    private fun clearSearch() {
        _searchedFilms.value = emptyList()
    }

    companion object {
        private const val FIRST_PAGE = 1
    }
}