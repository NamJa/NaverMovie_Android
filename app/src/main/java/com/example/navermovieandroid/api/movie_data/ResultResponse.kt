package com.example.navermovieandroid.api.movie_data

data class ResultResponse(
    var title: String,
    var link: String,
    var image: String,
    var director: String,
    var actor: String,
    var userRating: String,
    var movNum: String,
    var isWished: Boolean = false
)