package com.example.bobthedefender.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.bobthedefender.R
import com.example.bobthedefender.databinding.FragmentResultsBinding
import com.example.bobthedefender.ui.helpers.SharedPrefsManager
import com.example.bobthedefender.ui.viewmodels.GameViewModel
import com.example.bobthedefender.ui.viewmodels.ViewModelFactory

class ResultsFragment : Fragment() {
    private var _binding: FragmentResultsBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var gameViewModel: GameViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val viewModelProvider = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(
                context.getSharedPreferences(
                    SharedPrefsManager.GAME_PREFERENCES,
                    Context.MODE_PRIVATE
                )
            )
        )
        gameViewModel = viewModelProvider[GameViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentResultsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        val sharedPreferences = requireActivity().getSharedPreferences(
            SharedPrefsManager.GAME_PREFERENCES,
            Context.MODE_PRIVATE
        )

        binding.aliensKilled.text = SharedPrefsManager.getTotalKills(sharedPreferences).toString()
        binding.coinsEarned.text = SharedPrefsManager.getTotalCoins(sharedPreferences).toString()
        binding.bulletsShot.text = SharedPrefsManager.getTotalShots(sharedPreferences).toString()

        Glide.with(requireContext())
            .asGif()
            .load(R.raw.alien_192x192)
            .into(binding.alienView)

        Glide.with(requireContext())
            .asGif()
            .load(R.raw.fire_animation)
            .into(binding.fireView)

        Glide.with(requireContext())
            .asGif()
            .load(R.raw.coin)
            .into(binding.coinsView)
    }

}