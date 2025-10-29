package com.howdiedoodies.chatterby.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.howdiedoodies.chatterby.api.ChaturbateApi
import com.howdiedoodies.chatterby.databinding.FragmentRoomBinding
import com.howdiedoodies.chatterby.viewmodel.RoomViewModel
import kotlinx.coroutines.launch

class RoomFragment : Fragment() {
    private val args: RoomFragmentArgs by navArgs()
    private val viewModel: RoomViewModel by viewModels()
    private var _binding: FragmentRoomBinding? = null
    private val binding get() = _binding!!
    private lateinit var chatAdapter: ChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRoomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chatAdapter = ChatAdapter()
        binding.chatRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = chatAdapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.events.collect { events ->
                chatAdapter.submitList(events)
            }
        }

        viewModel.startPolling(args.username)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
