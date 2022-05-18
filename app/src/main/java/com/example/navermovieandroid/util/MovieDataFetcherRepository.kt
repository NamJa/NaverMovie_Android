package com.example.navermovieandroid.util

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.navermovieandroid.api.NaverMovieAPI
import com.example.navermovieandroid.api.movie_data.MovieResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "MovieDataFetcherRepository"

class MovieDataFetcherRepository {

    private val naverMovieAPI: NaverMovieAPI
    val _movieData: MutableLiveData<MovieResponse> = MutableLiveData()

    init {
        val client = OkHttpClient.Builder().addInterceptor(object : Interceptor{
            override fun intercept(chain: Interceptor.Chain): Response {
                val original = chain.request()
                val request = original.newBuilder()
                    .header("X-Naver-Client-Id", "sek_HBuIAhp2ClXvgxmN")
                    .header("X-Naver-Client-Secret", "0O1_a_4cRX")
                    .build()

                return chain.proceed(request)
            }
        }).build()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://openapi.naver.com/v1/search/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        naverMovieAPI = retrofit.create(NaverMovieAPI::class.java)
    }

    fun fetchMovieData(query: String, start: Int, display: Int) {
        naverMovieAPI.fetchMovieData(query, start, display).enqueue(object : Callback<MovieResponse> {
            override fun onResponse(
                call: Call<MovieResponse>,
                response: retrofit2.Response<MovieResponse>
            ) {
                if (response.isSuccessful) {
                    val movieRes: MovieResponse? = response.body()
                    movieRes?.let {
                        _movieData.value = it
                    }
                }
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Log.e(TAG, "failed to receive Naver Movie Data", t)
            }
        })
    }
}