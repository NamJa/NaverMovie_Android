package com.example.navermovieandroid

import androidx.lifecycle.ViewModel
import com.example.navermovieandroid.util.MovieDatabaseRepository

class ShowFavoritesViewModel: ViewModel() {
    private val movieDatabaseRepository = MovieDatabaseRepository.get()

    val favoritesDB = movieDatabaseRepository.getMovieFavorites()
}