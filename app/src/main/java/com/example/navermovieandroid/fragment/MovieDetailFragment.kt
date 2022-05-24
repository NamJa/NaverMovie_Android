package com.example.navermovieandroid.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.navermovieandroid.R
import com.example.navermovieandroid.adapter.MovieRecyclerViewAdapter
import com.example.navermovieandroid.api.movie_data.ResultResponse
import com.example.navermovieandroid.databinding.FragmentMovieDetailBinding

private const val ARG_PARAM1 = "param1"

class MovieDetailFragment : Fragment() {

    private lateinit var binding: FragmentMovieDetailBinding

    private lateinit var resItem: ResultResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /** 인자로 전달한 ResultResponse 객체를 수신합니다. */
        arguments?.let {
            resItem = it.getParcelable(ARG_PARAM1) ?: ResultResponse("","","","","","","",false)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie_detail, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

        /** 임의로 생성한 Toolbar의 초기 세팅 코드입니다. */
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.toolbar.setNavigationOnClickListener {
            (activity as AppCompatActivity).onBackPressed()
        }
        binding.toolbar.title = ""
        binding.toolbarTextTitle.text = resItem.title


        val adapter = MovieRecyclerViewAdapter(requireContext())
        adapter.setList(listOf(resItem))
        /** recyclerView 아이템의 클릭 동작을 막아, 실행된 fragment가 재실행되는 것을 막습니다. */
        adapter.isClickable = false
        binding.recyclerView.adapter = adapter


        /** webView 동작 */
        binding.webView.apply {
            true.also { settings.javaScriptEnabled = it }
            webViewClient = WebViewClient()
            webChromeClient = WebChromeClient()
        }
        binding.webView.loadUrl(resItem.link)
    }

    fun initView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: ResultResponse) =
            MovieDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1, param1)
                }
            }
    }
}