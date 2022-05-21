package com.example.navermovieandroid

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.navermovieandroid.adapter.MovieRecyclerViewAdapter

class ShowFavoritesFragment : Fragment() {

    private lateinit var closeBtn: ImageView
    private lateinit var favoritesRecyclerView: RecyclerView

    private lateinit var viewModel: ShowFavoritesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ShowFavoritesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_show_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)

        closeBtn.setOnClickListener {
            (activity as AppCompatActivity).onBackPressed()
        }

        viewModel.favoritesDB.observe(
            viewLifecycleOwner,
            Observer {
                favoritesRecyclerView.adapter = MovieRecyclerViewAdapter(requireContext(), it)
            }
        )

    }

    fun initView(view: View) {
        closeBtn = view.findViewById(R.id.closeBtn)
        favoritesRecyclerView = view.findViewById(R.id.favoritesRecyclerView)
        favoritesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    companion object {
        @JvmStatic
        fun newInstance() = ShowFavoritesFragment()
    }
}