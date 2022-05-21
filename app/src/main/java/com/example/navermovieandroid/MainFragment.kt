package com.example.navermovieandroid

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.navermovieandroid.adapter.MovieRecyclerViewAdapter
import com.example.navermovieandroid.api.movie_data.ResultResponse
import com.example.navermovieandroid.viewmodels.MainFragmentViewModel
import com.google.android.material.textfield.TextInputLayout

private const val TAG = "MainFragment"

class MainFragment : Fragment() {

    interface FavoritesCallback {
        fun onShowFavoritesBtnClicked()
    }

    private lateinit var inputLayout: TextInputLayout
    private lateinit var editText: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var showFavoritesBtn: LinearLayout


    private lateinit var viewModel: MainFragmentViewModel
    private var favCallback: FavoritesCallback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        favCallback = context as FavoritesCallback?
    }

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

        showFavoritesBtn.setOnClickListener {
            favCallback?.onShowFavoritesBtnClicked()
        }

        viewModel.movieData.observe(
            viewLifecycleOwner,
            Observer {
                if (it.items.size > 0) {
                    for (i in 0 until it.items.size) {
                        it.items[i] = stringProcess(it.items[i])
                    }
                }
                recyclerView.adapter = MovieRecyclerViewAdapter(requireContext(), it.items)
            }
        )
    }

    private fun initView(view: View) {
        inputLayout = view.findViewById(R.id.tiLayout)
        editText = inputLayout.editText!!
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        showFavoritesBtn = view.findViewById(R.id.showFavoritesBtn)
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


    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }
}