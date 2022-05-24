package com.example.navermovieandroid.adapter

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.navermovieandroid.R
import com.example.navermovieandroid.api.movie_data.ResultResponse
import com.example.navermovieandroid.databinding.ItemRecyclerviewMovieBinding
import com.example.navermovieandroid.util.MovieDatabaseRepository
import org.json.JSONArray
import org.json.JSONException

class MovieRecyclerViewAdapter(
    var context: Context
    ) : RecyclerView.Adapter<MovieRecyclerViewAdapter.MovieDataViewHolder>() {

    interface Callbacks{
        fun onMovieItemSelected(resItem: ResultResponse)
    }
    private var callbacks: Callbacks = context as Callbacks

    private var movieList: MutableList<ResultResponse> = mutableListOf()
    var isClickable = true

    inner class MovieDataViewHolder(private val binding: ItemRecyclerviewMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        val movieDatabaseRepository = MovieDatabaseRepository.get()

        fun bind(resItem: ResultResponse) {

            /** recycler Item 클릭 이벤트 */
            itemView.setOnClickListener {
                if(isClickable)
                    callbacks.onMovieItemSelected(resItem)
            }

            /** 영화 포스터 이미지 할당 */
            Glide.with(itemView)
                .load(resItem.image)
                .placeholder(R.drawable.no_image_placeholder)
                .into(binding.moviePoster)

            binding.movieTitle.text = resItem.title
            binding.movieDirector.text = context.getString(R.string.director, resItem.director)
            binding.movieActor.text = context.getString(R.string.actor, resItem.actor)
            binding.movieRate.text = context.getString(R.string.userRated, resItem.userRating)


            /** sharedPreferences에서 가져온 데이터에 찜 처리가 되어있다면 목록 아이템 속성에 true 대입 */
            resItem.isWished = getWishedListPref(context).contains(resItem.movNum)
            /** 즐겨찾기 버튼 이미지 처리 */
            setFavoriteBtnImage(itemView, resItem.isWished)
            /** 즐겨찾기 버튼 클릭 이벤트 처리 */
            binding.favoriteBtn.setOnClickListener {
                if (resItem.isWished) { // 이미 북마크한 경우
                    // SharedPreference 데이터 작업
                    setWishedListPref(context, resItem.movNum, "DELETE")
                    resItem.isWished = false
                    setFavoriteBtnImage(itemView, resItem.isWished)
                    // Room database 작업
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
                .into(binding.favoriteBtn)
        }
        /** SharedPreference의 데이터를 가져옵니다. */
        fun getWishedListPref(context: Context): MutableList<String> {
            val prefs: SharedPreferences = context.getSharedPreferences(
                context.getString(R.string.preference_key),
                Context.MODE_PRIVATE
            )
            // 위에서 가져온 sharedPreference데이터를 JSON으로 변환하여,
            // 수정 가능한 MutableList로 반환합니다.
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
                if (state == "ADD") { // state 인자에 따라 추가 및 제거 동작이 결정됩니다.
                    wishedList.add(movNum)
                } else { // state 인자에 따라 추가 및 제거 동작이 결정됩니다.
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

    fun setList(itemList: List<ResultResponse>) {
        movieList = itemList.toMutableList()
    }

    fun clear() {
        movieList.clear()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieDataViewHolder {
        val binding = DataBindingUtil.inflate<ItemRecyclerviewMovieBinding>(LayoutInflater.from(context), R.layout.item_recyclerview_movie, parent, false)
        return MovieDataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieDataViewHolder, position: Int) {
        holder.bind(movieList[position])
    }

    override fun getItemCount(): Int {
        return movieList.size
    }
}