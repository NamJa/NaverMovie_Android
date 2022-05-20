package com.example.navermovieandroid.api.movie_data

class MovieResponse {
    var total: Int = 0
    var start: Int = 0
    var display: Int = 0
    lateinit var items: MutableList<ResultResponse>
}