package com.citrus.skillcinema.data.models

object MovieData {
    private val countries = mapOf(
        1 to "США",
        2 to "Швейцарии",
        3 to "Франции",
        4 to "Польши",
        5 to "Великобритании",
        6 to "Швеции",
        7 to "Индии",
        8 to "Испании",
        9 to "Германии",
        10 to "Италии",
        11 to "Гонконга",
        12 to "Германии(ФРГ)",
        13 to "Австралии",
        14 to "Канады",
        15 to "Мексики",
        34 to "России"
    )

    private val countriesInNominative = mapOf(
        1 to "США",
        2 to "Швейцария",
        3 to "Франция",
        4 to "Польша",
        5 to "Великобритания",
        6 to "Швеция",
        7 to "Индия",
        8 to "Испания",
        9 to "Германия",
        10 to "Италия",
        11 to "Гонконг",
        12 to "Германия (ФРГ)",
        13 to "Австралия",
        14 to "Канада",
        15 to "Мексика",
        34 to "Россия"
    )
    private val genres = mapOf(
        1 to "Триллеры",
        2 to "Драмы",
        3 to "Криминал",
        4 to "Мелодрамы",
        5 to "Детективы",
        6 to "Фантастика",
        7 to "Приключения",
        8 to "Биографии",
        9 to "Нуары",
        10 to "Вестерны",
        11 to "Боевики",
        12 to "Фэнтези",
        13 to "Комедии",
        14 to "Военные фильмы",
        15 to "Исторические фильмы",
    )


    fun getCountryName(country: Int): String {
        return countries[country] ?: "Неизвестная страна"
    }

    fun getGenreName(genre: Int): String {
        return genres[genre] ?: "Неизвестный жанр"
    }
    fun getCountryInNominative(country: Int) : String {
        return countriesInNominative[country] ?: "Неизвестная страна"
    }
}

class CountryAndGenreList{
    val countryList: List<List<Int>> = listOf(
        listOf(1),
        listOf(2),
        listOf(3),
        listOf(4),
        listOf(5),
        listOf(6),
        listOf(7),
        listOf(8),
        listOf(9),
        listOf(10),
        listOf(11),
        listOf(12),
        listOf(13),
        listOf(14),
        listOf(15),
        listOf(34)
    )

    val genreList: List<List<Int>> = listOf(
        listOf(1),
        listOf(2),
        listOf(3),
        listOf(4),
        listOf(5),
        listOf(6),
        listOf(7),
        listOf(8),
        listOf(9),
        listOf(10),
        listOf(11),
        listOf(12),
        listOf(13),
        listOf(14),
        listOf(15),
        listOf(34)
    )
}

class FilmType {
    val allTypes = "ALL"
}
class FilmsOrder {
    val rating = "RATING"
    val year = "YEAR"
}


