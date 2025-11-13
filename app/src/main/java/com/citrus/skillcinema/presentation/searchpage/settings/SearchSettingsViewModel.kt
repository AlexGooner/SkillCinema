package com.citrus.skillcinema.presentation.searchpage.settings

import androidx.lifecycle.ViewModel

class SearchSettingsViewModel : ViewModel() {
    var country: String? = null
    var genre: String? = null
    var yearFrom: Int? = null
    var yearTo: Int? = null
    var selectedType: String = "ALL"
    var selectedOrder: String = "RATING"
    var ratingFrom : Int? = 1
    var ratingTo: Int? = 10
}