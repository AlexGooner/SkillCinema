package com.citrus.skillcinema.presentation.gallerypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.citrus.skillcinema.data.MovieRepository
import com.citrus.skillcinema.data.models.GalleryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryFullViewModel @Inject constructor(private val repository: MovieRepository) :
    ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _asStillList = MutableStateFlow<List<GalleryItem>>(emptyList())
    val asStillList = _asStillList.asStateFlow()

    private val _asShootingList = MutableStateFlow<List<GalleryItem>>(emptyList())
    val asShootingList = _asShootingList.asStateFlow()

    private val _asPosterList = MutableStateFlow<List<GalleryItem>>(emptyList())
    val asPosterList = _asPosterList.asStateFlow()

    private val _asFanArtList = MutableStateFlow<List<GalleryItem>>(emptyList())
    val asFanArtList = _asFanArtList.asStateFlow()

    private val _asPromoList = MutableStateFlow<List<GalleryItem>>(emptyList())
    val asPromoList = _asPromoList.asStateFlow()

    private val _asConceptList = MutableStateFlow<List<GalleryItem>>(emptyList())
    val asConceptList = _asConceptList.asStateFlow()

    private val _asWallpaperList = MutableStateFlow<List<GalleryItem>>(emptyList())
    val asWallpaperList = _asWallpaperList.asStateFlow()

    private val _asCoverList = MutableStateFlow<List<GalleryItem>>(emptyList())
    val asCoverList = _asCoverList.asStateFlow()

    private val _asScreenshotList = MutableStateFlow<List<GalleryItem>>(emptyList())
    val asScreenshotList = _asScreenshotList.asStateFlow()


    fun loadShootings(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            runCatching {
                val shootingList = repository.getImages(movieId, "SHOOTING", 1)
                _asShootingList.value = shootingList
            }
            _isLoading.value = false
        }
    }

    fun loadPosters(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            runCatching {
                val postersList = repository.getImages(movieId, "POSTER", 1)
                _asPosterList.value = postersList
            }
            _isLoading.value = false
        }
    }

    fun loadStills(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            runCatching {
                val stillList = repository.getImages(movieId, "STILL", 1)
                _asStillList.value = stillList
            }
            _isLoading.value = false
        }
    }

    fun loadFanArts(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            runCatching {
                val fanArts = repository.getImages(movieId, "FAN_ART", 1)
                _asFanArtList.value = fanArts
            }
            _isLoading.value = false
        }
    }

    fun loadPromos(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            runCatching {
                val promos = repository.getImages(movieId, "PROMO", 1)
                _asPromoList.value = promos
            }
            _isLoading.value = false
        }
    }

    fun loadConcepts(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            runCatching {
                val concepts = repository.getImages(movieId, "CONCEPT", 1)
                _asConceptList.value = concepts
            }
            _isLoading.value = false
        }
    }

    fun loadWallpapers(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            runCatching {
                val wallpapers = repository.getImages(movieId, "WALLPAPER", 1)
                _asWallpaperList.value = wallpapers
            }
            _isLoading.value = false
        }
    }

    fun loadCovers(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            runCatching {
                val covers = repository.getImages(movieId, "COVER", 1)
                _asCoverList.value = covers
            }
            _isLoading.value = false
        }
    }

    fun loadScreenshots(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            runCatching {
                val screenshots = repository.getImages(movieId, "SCREENSHOT", 1)
                _asScreenshotList.value = screenshots
            }
            _isLoading.value = false
        }
    }


}