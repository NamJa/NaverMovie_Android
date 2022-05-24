package com.example.navermovieandroid

import android.app.Application
import com.example.navermovieandroid.util.MovieDatabaseRepository

class MasterApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        /** 앱 실행 시 Room DB를 초기화 */
        MovieDatabaseRepository.initialize(this)
    }
}