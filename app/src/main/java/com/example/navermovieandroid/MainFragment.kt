package com.example.navermovieandroid

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.navermovieandroid.adapter.MovieRecyclerViewAdapter
import com.example.navermovieandroid.api.movie_data.ResultResponse
import com.example.navermovieandroid.databinding.FragmentMainBinding
import com.example.navermovieandroid.viewmodels.MainFragmentViewModel

private const val TAG = "MainFragment"

class MainFragment : Fragment() {

    interface FavoritesCallback {
        fun onShowFavoritesBtnClicked()
    }

    private var queryText = ""
    private var recyclerItemTotalCount = 0
    private var movieResTotal = 0
    private var start = 1
    private var display = 15

    private lateinit var binding: FragmentMainBinding

    private lateinit var editText: EditText

    private lateinit var movieListAdapter: MovieRecyclerViewAdapter
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        movieListAdapter = MovieRecyclerViewAdapter(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

        /** editText에서 enter키를 입력받았을 경우 */
        editText.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    queryText = editText.text.toString()
                    start = 1
                    display = 15
                    viewModel.isOnPaused = false
                    binding.recyclerView.adapter = movieListAdapter
                    viewModel.totalMovieData.clear()
                    movieListAdapter.clear()
                    viewModel.fetchMovieData(queryText, start, display)

                    return true
                }
                return false
            }
        })

        /** '즐겨찾기' 버튼 클릭 */
        binding.showFavoritesBtn.setOnClickListener {
            favCallback?.onShowFavoritesBtnClicked()
        }


        /** 영화 검색 데이터를 관찰하여 RecyclerView에 데이터를 추가한다. */
        viewModel.movieData.observe(
            viewLifecycleOwner,
            Observer {
                if (it.items.size > 0) {
                    for (i in 0 until it.items.size) {
                        it.items[i] = stringProcess(it.items[i])
                    }
                }
                movieResTotal = it.total
                if(it.display < display) {
                    display = it.display
                }

                if (!viewModel.isOnPaused) {
                    viewModel.totalMovieData.addAll(it.items)
                    movieListAdapter.notifyItemRangeInserted(recyclerItemTotalCount + 1, it.items.size)
                }
                movieListAdapter.setList(viewModel.totalMovieData)
            }
        )

        /** RecyclerView의 무한 스크롤 동작 */
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                recyclerItemTotalCount = viewModel.totalMovieData.size - 1

                if (!recyclerView.canScrollVertically(1) && (lastVisibleItemPosition == recyclerItemTotalCount) && (movieResTotal >= recyclerItemTotalCount+1)) {
                    if(movieResTotal-1 != lastVisibleItemPosition) {
                        viewModel.isOnPaused = false
                        start += display
                        viewModel.fetchMovieData(queryText, start, display)
                    }
                }
            }
        })
    }

    override fun onPause() {
        super.onPause()
        viewModel.isOnPaused = true
    }

    private fun initView() {
        editText = binding.tiLayout.editText!!
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = movieListAdapter
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