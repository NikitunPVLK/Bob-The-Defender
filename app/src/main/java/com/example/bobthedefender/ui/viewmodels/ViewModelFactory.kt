package com.example.bobthedefender.ui.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bobthedefender.ui.models.Player

class ViewModelFactory(
    private val sharedPreferences: SharedPreferences,
    private val player: Player
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FightViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FightViewModel(sharedPreferences, player) as T
        } else if (modelClass.isAssignableFrom(ShopViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ShopViewModel(sharedPreferences, player) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class" + modelClass::class.simpleName)
    }
}