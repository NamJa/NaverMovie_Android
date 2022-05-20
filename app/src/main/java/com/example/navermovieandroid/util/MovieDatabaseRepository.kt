package com.example.navermovieandroid.util

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.navermovieandroid.api.movie_data.ResultResponse
import com.example.navermovieandroid.database.MovieDatabase
import java.util.concurrent.Executors

private const val DATABASE_NAME = "moviefav-database"

class MovieDatabaseRepository private constructor(context: Context){

    private val database : MovieDatabase = Room.databaseBuilder(context.applicationContext, MovieDatabase::class.java, DATABASE_NAME).build()

    private val movieDao = database.movieDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun getMovieFavorites(): LiveData<List<ResultResponse>> = movieDao.getMovieFavorites()

    fun addMovFav(resItem: ResultResponse) {
        executor.execute { movieDao.addMovFav(resItem) }
    }
    fun delMovFav(resItem: ResultResponse) {
        executor.execute { movieDao.delMovFav(resItem) }
    }

    companion object{
        private var INSTANCE: MovieDatabaseRepository? = null

        fun initialize(context: Context) {
            if(INSTANCE == null) {
                INSTANCE = MovieDatabaseRepository(context)
            }
        }

        fun get(): MovieDatabaseRepository {
            return INSTANCE ?: throw IllegalStateException("MovieDatabaseRepository must be initialized")
        }
    }
}