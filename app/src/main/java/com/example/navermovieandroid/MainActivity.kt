package com.example.navermovieandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity(), MainFragment.FavoritesCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.mainContainer, MainFragment.newInstance())
            .commit()
    }

    override fun onShowFavoritesBtnClicked() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainContainer, ShowFavoritesFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }
}