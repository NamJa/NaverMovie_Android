package com.example.navermovieandroid

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.navermovieandroid.api.movie_data.ResultResponse

private const val ARG_PARAM1 = "param1"

class MovieDetailFragment : Fragment() {
    private lateinit var resItem: ResultResponse

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

        Log.d("hou", "${resItem.title}, ${resItem.director}, ${resItem.userRating}")
        return inflater.inflate(R.layout.fragment_movie_detail, container, false)
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