package com.example.navermovieandroid

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.navermovieandroid.api.movie_data.MovieResponse
import com.example.navermovieandroid.util.MovieDataFetcherRepository

class MainFragmentViewModel: ViewModel() {

    private val movieDataFetcherRepository = MovieDataFetcherRepository()
    val movieData: LiveData<MovieResponse>
        get() = movieDataFetcherRepository._movieData

    fun fetchMovieData(query: String, start: Int, display: Int) {
        movieDataFetcherRepository.fetchMovieData(query, start, display)
    }

}