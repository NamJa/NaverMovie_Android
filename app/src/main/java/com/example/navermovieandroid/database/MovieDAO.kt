package com.example.navermovieandroid.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.navermovieandroid.api.movie_data.ResultResponse

@Dao
interface MovieDAO {
    @Query("SELECT * FROM ResultResponse")
    fun getMovieFavorites(): LiveData<List<ResultResponse>>

    @Insert
    fun addMovFav(resItem: ResultResponse)

    @Delete
    fun delMovFav(resItem: ResultResponse)
}