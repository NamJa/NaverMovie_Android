package com.example.navermovieandroid.adapter

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.navermovieandroid.R
import com.example.navermovieandroid.api.movie_data.ResultResponse
import com.example.navermovieandroid.util.MovieDatabaseRepository
import org.json.JSONArray
import org.json.JSONException

class MovieRecyclerViewAdapter(
    var context: Context,
    var movieList: List<ResultResponse>
    ) : RecyclerView.Adapter<MovieRecyclerViewAdapter.MovieDataViewHolder>() {

    interface Callbacks{
        fun onMovieItemSelected(resItem: ResultResponse)
    }
    private var callbacks: Callbacks = context as Callbacks

    inner class MovieDataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val movieDatabaseRepository = MovieDatabaseRepository.get()
        val moviePoster: ImageView
        val movieTitle: TextView
        val movieDirector: TextView
        val movieActor: TextView
        val movieRate: TextView
        val favoriteBtn: ImageView

        init {
            moviePoster = itemView.findViewById(R.id.moviePoster)
            movieTitle = itemView.findViewById(R.id.movieTitle)
            movieDirector = itemView.findViewById(R.id.movieDirector)
            movieActor = itemView.findViewById(R.id.movieActor)
            movieRate = itemView.findViewById(R.id.movieRate)
            favoriteBtn = itemView.findViewById(R.id.favoriteBtn)
        }

        fun bind(resItem: ResultResponse) {
            Glide.with(itemView)
                .load(resItem.image)
                .into(moviePoster)

            movieTitle.text = resItem.title
            movieDirector.text = "감독: ${resItem.director}"
            movieActor.text = "출연: ${resItem.actor}"
            movieRate.text = "평점: ${resItem.userRating}"

            itemView.setOnClickListener {
                callbacks.onMovieItemSelected(resItem)
            }

            /** sharedPreferences에서 가져온 데이터에 찜 처리가 되어있다면 목록 아이템 속성에 true 대입 */
            resItem.isWished = getWishedListPref(context).contains(resItem.movNum)
            /** 즐겨찾기 버튼 이미지 처리 */
            setFavoriteBtnImage(itemView, resItem.isWished)
            /** 즐겨찾기 버튼 클릭 이벤트 처리 */
            favoriteBtn.setOnClickListener {
                if (resItem.isWished) { // 이미 북마크한 경우
                    setWishedListPref(context, resItem.movNum, "DELETE")
                    resItem.isWished = false
                    setFavoriteBtnImage(itemView, resItem.isWished)
                    movieDatabaseRepository.delMovFav(resItem)
                } else {                  // 북마크 안한 경우
                    setWishedListPref(context, resItem.movNum, "ADD")
                    resItem.isWished = true
                    setFavoriteBtnImage(itemView, resItem.isWished)
                    movieDatabaseRepository.addMovFav(resItem)
                }
            }

        }
        fun setFavoriteBtnImage(itemView: View, isWished: Boolean) {
            val wishedBtnImage = if (isWished) {
                AppCompatResources.getDrawable(context, R.drawable.star_yellow)
            } else {
                AppCompatResources.getDrawable(context, R.drawable.star_gray)
            }
            Glide.with(itemView)
                .load(wishedBtnImage)
                .into(favoriteBtn)
        }
        /** SharedPreference의 데이터를 가져옵니다. */
        fun getWishedListPref(context: Context): MutableList<String> {
            val prefs: SharedPreferences = context.getSharedPreferences(
                context.getString(R.string.preference_key),
                Context.MODE_PRIVATE
            )
            val json = prefs.getString(context.getString(R.string.preference_key), "")
            val wishedList: MutableList<String> = mutableListOf()
            if (json != "") {
                try {
                    val jsonArray = JSONArray(json)
                    for (i in 0..jsonArray.length()) {
                        val movNum = jsonArray.optString(i)
                        wishedList.add(movNum)
                    }
                } catch (E: JSONException) {
                    Log.e("GoodsItemRecyclerAdapter", "Cannot parse JSONArray", E)
                }
            }
            return wishedList
        }

        /** SharedPreference에 데이터를 추가합니다. */
        fun setWishedListPref(context: Context, movNum: String, state: String) {
            val sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_key),
                Context.MODE_PRIVATE
            )
            with(sharedPref.edit()) {
                val wishedList = getWishedListPref(context)
                if (state == "ADD") {
                    wishedList.add(movNum)
                } else {
                    wishedList.remove(movNum)
                }
                val jsonArray = JSONArray()
                for (i in wishedList) {
                    jsonArray.put(i)
                }
                putString(context.getString(R.string.preference_key), jsonArray.toString())
                apply()
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieDataViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_recyclerview_movie, parent, false)
        return MovieDataViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieDataViewHolder, position: Int) {
        holder.bind(movieList[position])
    }

    override fun getItemCount(): Int {
        return movieList.size
    }
}