package com.example.navermovieandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.navermovieandroid.adapter.MovieRecyclerViewAdapter
import com.example.navermovieandroid.api.movie_data.ResultResponse
import com.example.navermovieandroid.fragment.MainFragment
import com.example.navermovieandroid.fragment.MovieDetailFragment
import com.example.navermovieandroid.fragment.ShowFavoritesFragment

class MainActivity : AppCompatActivity(), MainFragment.FavoritesCallback, MovieRecyclerViewAdapter.Callbacks {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /** Activity 실행 시 바로 MainFragment로 진입합니다. */
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainContainer, MainFragment.newInstance())
            .commit()
    }

    /** 즐겨찾기 버튼 클릭 시 동작 */
    override fun onShowFavoritesBtnClicked() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainContainer, ShowFavoritesFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }

    /** recyclerView 아이템 클릭 시 동작 */
    override fun onMovieItemSelected(resItem: ResultResponse) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainContainer, MovieDetailFragment.newInstance(resItem))
            .addToBackStack(null)
            .commit()
    }
}