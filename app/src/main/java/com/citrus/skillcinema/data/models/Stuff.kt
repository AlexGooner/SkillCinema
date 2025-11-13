package com.citrus.skillcinema.data.models


data class Stuff(
    val staffId: Int,
    val nameRu: String?,
    val nameEn: String?,
    val description: String?,
    val posterUrl: String?,
    val professionText: String?,
    val professionKey: String?,
    val professionKeys : List<String?> = emptyList()
)

