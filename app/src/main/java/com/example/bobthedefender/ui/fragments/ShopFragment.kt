package com.example.bobthedefender.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bobthedefender.databinding.FragmentShopBinding
import com.example.bobthedefender.domain.BtdApplication
import com.example.bobthedefender.ui.fragments.adapters.WeaponListAdapter
import com.example.bobthedefender.ui.helpers.SharedPrefsManager
import com.example.bobthedefender.ui.viewmodels.FightViewModel
import com.example.bobthedefender.ui.viewmodels.ShopViewModel
import com.example.bobthedefender.ui.viewmodels.ViewModelFactory

class ShopFragment : Fragment() {
    private var _binding: FragmentShopBinding? = null
    private val binding: FragmentShopBinding
        get() = _binding!!

    private lateinit var fightViewModel: FightViewModel
    private lateinit var shopViewModel: ShopViewModel

    private lateinit var adapter: WeaponListAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val viewModelProvider =
            ViewModelProvider(
                requireActivity(),
                ViewModelFactory(
                    context.getSharedPreferences(
                        SharedPrefsManager.GAME_PREFERENCES,
                        Context.MODE_PRIVATE
                    ),
                    (requireActivity().application as BtdApplication).player
                )
            )
        shopViewModel = viewModelProvider[ShopViewModel::class.java]
        fightViewModel = viewModelProvider[FightViewModel::class.java]
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
        shopViewModel.coins.observe(viewLifecycleOwner) {
            binding.coins.text = it.toString()
        }
        super.onViewCreated(view, savedInstanceState)

        adapter = WeaponListAdapter(
            resources,
            shopViewModel::isBuyEnabled,
            shopViewModel::buyWeapon
        )

        binding.weaponList.adapter = adapter
        binding.weaponList.layoutManager =
            GridLayoutManager(context, 3)

        shopViewModel.catalog.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}