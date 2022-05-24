package com.example.navermovieandroid.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.navermovieandroid.R
import com.example.navermovieandroid.adapter.MovieRecyclerViewAdapter
import com.example.navermovieandroid.databinding.FragmentShowFavoritesBinding
import com.example.navermovieandroid.viewmodels.ShowFavoritesViewModel

class ShowFavoritesFragment : Fragment() {

    private lateinit var binding: FragmentShowFavoritesBinding
    private lateinit var viewModel: ShowFavoritesViewModel
    private lateinit var movieListAdapter: MovieRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ShowFavoritesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        movieListAdapter = MovieRecyclerViewAdapter(requireContext())
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_show_favorites, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

        /** 'X'버튼의 동작 */
        binding.closeBtn.setOnClickListener {
            (activity as AppCompatActivity).onBackPressed()
        }

        /** 즐겨찾기 목록을 관찰하여 RecyclerView에 할당합니다. */
        viewModel.favoritesDB.observe(
            viewLifecycleOwner,
            Observer {
                movieListAdapter.setList(it)
                binding.favoritesRecyclerView.adapter = movieListAdapter
            }
        )

    }

    fun initView() {
        binding.favoritesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    companion object {
        @JvmStatic
        fun newInstance() = ShowFavoritesFragment()
    }
}