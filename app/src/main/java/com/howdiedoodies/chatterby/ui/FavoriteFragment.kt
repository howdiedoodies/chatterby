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
import com.howdiedoodies.chatterby.databinding.FragmentFavoriteBinding
import com.howdiedoodies.chatterby.viewmodel.FavoriteViewModel
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment() {
    private val viewModel: FavoriteViewModel by viewModels()
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var favoriteAdapter: FavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteAdapter = FavoriteAdapter { favorite ->
            val action = FavoriteFragmentDirections.actionFavoriteFragmentToRoomFragment(favorite.username)
            findNavController().navigate(action)
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = favoriteAdapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.favorites.collect {
                favoriteAdapter.submitList(it)
            }
        }

        binding.refreshLayout.setOnRefreshListener {
            viewModel.refreshFavorites()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
