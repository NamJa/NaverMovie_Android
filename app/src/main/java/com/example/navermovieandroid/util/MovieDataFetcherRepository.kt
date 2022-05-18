package com.example.navermovieandroid.util

import com.example.navermovieandroid.api.NaverMovieAPI
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MovieDataFetcherRepository {

    private val naverMovieAPI: NaverMovieAPI

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
            .baseUrl("https://openapi.naver.com/v1/search/movie.json")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        naverMovieAPI = retrofit.create(NaverMovieAPI::class.java)
    }
}