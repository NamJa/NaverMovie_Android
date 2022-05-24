package com.example.navermovieandroid.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.navermovieandroid.api.movie_data.MovieResponse
import com.example.navermovieandroid.api.movie_data.ResultResponse
import com.example.navermovieandroid.util.MovieDataFetcherRepository

class MainFragmentViewModel: ViewModel() {

    private val movieDataFetcherRepository = MovieDataFetcherRepository()

    /** MutableLiveData를 LiveData로 가져옵니다. */
    val movieData: LiveData<MovieResponse>
        get() = movieDataFetcherRepository._movieData
    val totalMovieData: MutableList<ResultResponse> = mutableListOf()

    /** 상태 보존을 위한 Boolean 변수 */
    var isOnPaused = false

    /** 조건에 맞는 영화 데이터 수신 */
    fun fetchMovieData(query: String, start: Int, display: Int) {
        movieDataFetcherRepository.fetchMovieData(query, start, display)
    }

}