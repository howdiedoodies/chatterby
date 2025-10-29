package com.howdiedoodies.chatterby.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.howdiedoodies.chatterby.R
import com.howdiedoodies.chatterby.viewmodel.FavoriteViewModel
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment() {
    private val viewModel: FavoriteViewModel by viewModels()
    private lateinit var adapter: FavoriteAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FavoriteAdapter { username, chatOnly ->
            val action = FavoriteFragmentDirections.actionFavoriteFragmentToRoomFragment(username, chatOnly)
            findNavController().navigate(action)
        }

        view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recyclerView).apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@FavoriteFragment.adapter
        }

        lifecycleScope.launch {
            viewModel.favorites.collect {
                adapter.submitList(it)
            }
        }

        view.findViewById<SwipeRefreshLayout>(R.id.refresh).setOnRefreshListener {
            viewModel.refreshNow()
            view.findViewById<SwipeRefreshLayout>(R.id.refresh).isRefreshing = false
        }
    }
}
