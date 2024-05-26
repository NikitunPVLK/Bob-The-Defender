package com.example.bobthedefender.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.bobthedefender.R
import com.example.bobthedefender.databinding.FragmentLobbyBinding

class LobbyFragment : Fragment() {
    private var _binding: FragmentLobbyBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLobbyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.startGameButton.setOnClickListener {
            startGame()
        }

        binding.shopButton.setOnClickListener {
            navigateToShop()
        }

        Glide.with(requireContext())
            .asGif()
            .load(R.raw.bob_x256)
            .into(binding.playerView)

        Glide.with(requireContext())
            .asGif()
            .load(R.raw.alien_192x192)
            .into(binding.alienView)
    }

    private fun navigateToShop() {
        findNavController().navigate(R.id.action_startFragment_to_shopScreenFragment)
    }

    private fun startGame() {
        findNavController().navigate(R.id.action_startFragment_to_gameScreenFragment)
    }
}