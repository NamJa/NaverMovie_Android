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
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.navermovieandroid.api.movie_data.ResultResponse
import com.google.android.material.textfield.TextInputLayout


class MainFragment : Fragment() {

    private lateinit var inputLayout: TextInputLayout
    private lateinit var editText: EditText
    private lateinit var recyclerView: RecyclerView

    private lateinit var viewModel: MainFragmentViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainFragmentViewModel::class.java)
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
        editText.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    val queryText = editText.text.toString()
                    viewModel.fetchMovieData(queryText, 1, 15)
                    return true
                }
                return false
            }
        })
        // 데이터 수신 확인용 Test Code
        viewModel.movieData.observe(
            viewLifecycleOwner,
            Observer {
                if (it.items.size > 0) {
                    for (i in 0 until it.items.size) {
                        it.items[i] = stringProcess(it.items[i])
                    }
                }
                recyclerView.adapter = MovieRecyclerViewAdapter(it.items)
            }
        )
    }


    private fun initView(view: View) {
        inputLayout = view.findViewById(R.id.tiLayout)
        editText = inputLayout.editText!!
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }


    fun stringProcess(resItem: ResultResponse): ResultResponse {
        resItem.movNum =
            resItem.link.replace("https://movie.naver.com/movie/bi/mi/basic.nhn?code=", "")
        resItem.title = resItem.title.replace("<b>", "")
        resItem.title = resItem.title.replace("</b>", "")
        resItem.director = if (resItem.director.isEmpty()) {
            "감독 정보가 없습니다."
        } else {
            resItem.director.replace("|", ",").removeSuffix(",")
        }
        resItem.actor = if (resItem.actor.isEmpty()) {
            "출연진 정보가 없습니다."
        } else {
            resItem.actor.replace("|", ",").removeSuffix(",")
        }
        return resItem
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Test code
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    inner class MovieRecyclerViewAdapter(var movieList: List<ResultResponse>) :
        RecyclerView.Adapter<MovieRecyclerViewAdapter.MovieDataViewHolder>() {
        inner class MovieDataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val moviePoster: ImageView
            val movieTitle: TextView
            val movieDirector: TextView
            val movieActor: TextView
            val movieRate: TextView

            init {
                moviePoster = itemView.findViewById(R.id.moviePoster)
                movieTitle = itemView.findViewById(R.id.movieTitle)
                movieDirector = itemView.findViewById(R.id.movieDirector)
                movieActor = itemView.findViewById(R.id.movieActor)
                movieRate = itemView.findViewById(R.id.movieRate)
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
                    Toast.makeText(requireContext(), "${resItem.movNum}", Toast.LENGTH_SHORT).show()
                }
            }
        }

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