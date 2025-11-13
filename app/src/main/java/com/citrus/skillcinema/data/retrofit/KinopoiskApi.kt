package com.citrus.skillcinema.data.retrofit

import com.citrus.skillcinema.data.models.Actor
import com.citrus.skillcinema.data.models.FilteredFilmsDto
import com.citrus.skillcinema.data.models.GalleryItemsDto
import com.citrus.skillcinema.data.models.Movie
import com.citrus.skillcinema.data.models.MovieList
import com.citrus.skillcinema.data.models.SeasonsInfoDto
import com.citrus.skillcinema.data.models.Stuff
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.http.Path

interface KinopoiskApi {

    @Headers("X-API-KEY: $api_key")
    @GET("/api/v2.2/films/collections")
    suspend fun getCollectionFilms(
        @Query(value = "type") type: String,
        @Query(value = "page") page: Int
    ): MovieList


    @Headers("X-API-KEY: $api_key")
    @GET("/api/v2.2/films/premieres")
    suspend fun getPremieres(@Query("year") year: Int, @Query("month") month: String): MovieList

    @Headers("X-API-KEY: $api_key")
    @GET("/api/v2.2/films")
    suspend fun getRandomGenreAndCountry(
        @Query("countries") country: Int,
        @Query("genres") genres: Int,
        @Query("page") page: Int
    ): MovieList

    @Headers("X-API-KEY: $api_key")
    @GET("/api/v2.2/films/{id}")
    suspend fun getFullInformationFilm(
        @Path(value = "id") id: Int
    ): Movie


    @Headers("X-API-KEY: $api_key")
    @GET("/api/v1/staff")
    suspend fun getStuff(
        @Query("filmId") filmId: Int,
    ): List<Stuff>

    @Headers("X-API-KEY: $api_key")
    @GET("/api/v2.2/films/{id}/images")
    suspend fun getImages(
        @Path("id") id: Int,
        @Query("type") type: String,
        @Query("page") page: Int
    ) : GalleryItemsDto

    @Headers("X-API-KEY: $api_key")
    @GET("/api/v2.2/films/{id}/similars")
    suspend fun getSimilarMovies(
        @Path("id") id: Int,
    ) : MovieList

    @Headers("X-API-KEY: $api_key")
    @GET("/api/v2.2/films/{id}/seasons")
    suspend fun getSeasonsInfo(
        @Path("id") id: Int
    ): SeasonsInfoDto

    @Headers("X-API-KEY: $api_key")
    @GET("/api/v1/staff/{id}")
    suspend fun getActorInfo(
        @Path("id") id: Int
    ): Actor

    @Headers("X-API-KEY: $api_key")
    @GET("/api/v2.2/films/{id}")
    suspend fun getFilmById(
        @Path("id") personId: Int
    ): Movie

    @Headers("X-API-KEY: $api_key")
    @GET("/api/v2.2/films")
    suspend fun getSearch(
        @Query("countries") countries: List<Int>,
        @Query("genres") genres: List<Int>,
        @Query("type") type: String,
        @Query("order") order: String,
        @Query("ratingFrom") ratingFrom: Int,
        @Query("ratingTo") ratingTo: Int,
        @Query("yearFrom") yearFrom: Int,
        @Query("yearTo") yearTo: Int,
        @Query("keyword") keyword: String,
        @Query("page") page: Int
    ) : FilteredFilmsDto



    companion object {
        const val api_key = "b049e92e-c67c-4e2e-892e-cd42068dbb36"
    }
}

private const val URL = "https://kinopoiskapiunofficial.tech"

val retrofit = Retrofit.Builder()
    .client(
        OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().also {
            it.level = HttpLoggingInterceptor.Level.BODY
        }).build()
    )
    .baseUrl(URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create(KinopoiskApi::class.java)


//"ca52b4b9-f327-49d7-a014-b02f7d9fd1cf" - original arsenal230698@mail.ru
//"cffd7d33-eef9-4355-b1b4-3e313d4011b2" - original gooner14190@gmail.com
//"e8d51abf-a138-4eb9-a81a-3c36263ccb4a" - original olesya.gorodnicheva@mail.ru
//"b049e92e-c67c-4e2e-892e-cd42068dbb36" - original bocharovalexand@yandex.ru