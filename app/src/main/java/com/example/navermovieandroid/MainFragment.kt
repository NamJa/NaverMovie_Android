package com.example.navermovieandroid

import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.navermovieandroid.api.movie_data.ResultResponse
import com.example.navermovieandroid.util.MovieDataFetcherRepository
import com.google.android.material.textfield.TextInputLayout


class MainFragment : Fragment() {

    private lateinit var inputLayout: TextInputLayout
    private lateinit var editText: EditText
    private lateinit var recyclerView: RecyclerView

    private val movieDataFetcherRepository = MovieDataFetcherRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)

        // editText에서 enter키를 입력받았을 경우
        editText.setOnKeyListener(object :View.OnKeyListener{
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if(keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_SEARCH) {
                    val queryText = editText.text.toString()
                    movieDataFetcherRepository.fetchMovieData(queryText,1,15)
                }
                return true
            }
        })
        // 데이터 수신 확인용 Test Code
        movieDataFetcherRepository._movieData.observe(
            viewLifecycleOwner,
            Observer {
                recyclerView.adapter = MovieRecyclerViewAdapter(it.items)
            }
        )
    }

    private fun initView(view: View){
        inputLayout = view.findViewById(R.id.tiLayout)
        editText = inputLayout.editText!!
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Test code
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    inner class MovieDataViewHolder(v: View): RecyclerView.ViewHolder(v) {
        val moviePoster: ImageView
        val movieTitle: TextView
        val movieDirector: TextView
        val movieActor: TextView
        val movieRate: TextView
        init {
            moviePoster = v.findViewById(R.id.moviePoster)
            movieTitle = v.findViewById(R.id.movieTitle)
            movieDirector = v.findViewById(R.id.movieDirector)
            movieActor = v.findViewById(R.id.movieActor)
            movieRate = v.findViewById(R.id.movieRate)
        }

        fun bind(resItem: ResultResponse) {
            Glide.with(itemView)
                .load(resItem.image)
                .into(moviePoster)

            movieTitle.text = resItem.title
            movieDirector.text = resItem.director
            movieActor.text = resItem.actor
            movieRate.text = resItem.userRating
        }
    }

    inner class MovieRecyclerViewAdapter(var movieList: List<ResultResponse>): RecyclerView.Adapter<MovieDataViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieDataViewHolder {
            val view = layoutInflater.inflate(R.layout.item_recyclerview_movie, parent, false)
            return MovieDataViewHolder(view)
        }

        override fun onBindViewHolder(holder: MovieDataViewHolder, position: Int) {
            holder.bind(movieList[position])
        }

        override fun getItemCount(): Int {
            return movieList.size
        }
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }
}