package com.example.navermovieandroid

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.navermovieandroid.adapter.MovieRecyclerViewAdapter
import com.example.navermovieandroid.api.movie_data.ResultResponse

private const val ARG_PARAM1 = "param1"

class MovieDetailFragment : Fragment() {
    private lateinit var resItem: ResultResponse
    private lateinit var webView: WebView
    private lateinit var recyclerView: RecyclerView
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            resItem = it.getParcelable(ARG_PARAM1) ?: ResultResponse("","","","","","","",false)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)

        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            (activity as AppCompatActivity).onBackPressed()
        }
        toolbar.title = ""
        toolbar.findViewById<TextView>(R.id.toolbarTextTitle).text = resItem.title

        val adapter = MovieRecyclerViewAdapter(requireContext(), listOf(resItem))
        adapter.isClickable = false
        recyclerView.adapter = adapter

        webView.apply {
            true.also { settings.javaScriptEnabled = it }
            webViewClient = WebViewClient()
            webChromeClient = WebChromeClient()
        }
        webView.loadUrl(resItem.link)
    }

    fun initView(view: View) {
        webView = view.findViewById(R.id.webView)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        toolbar = view.findViewById(R.id.toolbar)
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