package com.example.navermovieandroid.viewmodels

import androidx.lifecycle.ViewModel
import com.example.navermovieandroid.util.MovieDatabaseRepository

class ShowFavoritesViewModel: ViewModel() {
    /** Singleton으로 생성된 Room DB객체를 가져옵니다. */
    private val movieDatabaseRepository = MovieDatabaseRepository.get()

    val favoritesDB = movieDatabaseRepository.getMovieFavorites()
}