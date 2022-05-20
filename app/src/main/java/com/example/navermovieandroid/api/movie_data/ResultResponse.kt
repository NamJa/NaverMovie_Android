package com.example.navermovieandroid.api.movie_data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ResultResponse(
    var title: String,
    var link: String,
    var image: String,
    var director: String,
    var actor: String,
    var userRating: String,
    @PrimaryKey var movNum: String,
    var isWished: Boolean = false
)