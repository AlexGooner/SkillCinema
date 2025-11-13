package com.citrus.skillcinema.data.models

data class Actor(
    val personId: Int,
    val webUrl: String?,
    val nameRu: String?,
    val nameEn: String?,
    val sex: String,
    val posterUrl: String,
    val growth: String,
    val birthday: String,
    val age: Int,
    val birthplace: String,
    val death: String?,
    val hasAwards: Int,
    val profession: String,
    val facts: List<String>,
    val films: List<ActorFilm>,
    val spouses: List<ActorSpouses>
)
data class ActorFilm(
    val filmId: Int,
    val nameRu: String,
    val nameEn: String,
    val rating: String,
    val general: Boolean,
    val description: String,
    val professionKey: String
)

data class ActorSpouses(
    val personId: Int,
    val name: String,
    val divorced: Boolean,
    val divorcedReason: String?,
    val sex: String,
    val children: Int,
    val webUrl: String?,
    val relation: String
)

