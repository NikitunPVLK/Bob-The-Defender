package com.example.bobthedefender.ui.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(private val sharedPreferences: SharedPreferences) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FightViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FightViewModel(sharedPreferences) as T
        } else if (modelClass.isAssignableFrom(ShopViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ShopViewModel(sharedPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class" + modelClass::class.simpleName)
    }
}