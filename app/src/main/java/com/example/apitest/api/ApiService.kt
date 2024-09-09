package com.example.apitest.api

import com.example.apitest.response.FilmDetayResponse
import com.example.apitest.response.FilmListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("movie/popular")
     fun getPopularMovies(
        @Query("page") page: Int
    ): Call<FilmListResponse>
     @GET("movie/{movie_id}")
     fun getMovieDetails(
        @Path("movie_id") id: Int
    ): Call<FilmDetayResponse>
}