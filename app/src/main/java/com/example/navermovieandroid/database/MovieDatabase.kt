package com.example.navermovieandroid.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.navermovieandroid.api.movie_data.ResultResponse

@Database(entities = [ResultResponse::class], version = 1)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDAO
}