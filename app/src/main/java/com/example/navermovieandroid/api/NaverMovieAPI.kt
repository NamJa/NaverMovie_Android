package com.example.navermovieandroid.api

import com.example.navermovieandroid.api.movie_data.MovieResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NaverMovieAPI {
    @GET("")
    fun fetchMovieData(
        @Query("query") query: String,
        @Query("start") start: Int,
        @Query("display") display: Int
    ): Call<MovieResponse>
}