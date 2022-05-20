package com.example.navermovieandroid

import android.app.Application
import com.example.navermovieandroid.util.MovieDatabaseRepository

class MasterApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        MovieDatabaseRepository.initialize(this)
    }
}