package com.example.bobthedefender.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bobthedefender.databinding.FragmentShopBinding
import com.example.bobthedefender.ui.fragments.adapters.WeaponListAdapter
import com.example.bobthedefender.ui.viewmodels.GameViewModel

class ShopFragment: Fragment() {
    private var _binding: FragmentShopBinding? = null
    private val binding: FragmentShopBinding
        get() = _binding!!

    private lateinit var gameViewModel: GameViewModel

    private lateinit var adapter: WeaponListAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val viewModelProvider = ViewModelProvider(requireActivity())
        gameViewModel = viewModelProvider[GameViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShopBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        gameViewModel.coins.observe(viewLifecycleOwner) {
            binding.coins.text = it.toString()
        }
        super.onViewCreated(view, savedInstanceState)

        adapter = WeaponListAdapter(gameViewModel::onItemBought)

        binding.weaponList.adapter = adapter
        binding.weaponList.layoutManager = LinearLayoutManager(context)

        adapter.submitList(gameViewModel.catalog)
    }
}