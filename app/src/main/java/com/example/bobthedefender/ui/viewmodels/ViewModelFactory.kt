package com.example.bobthedefender.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FightViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FightViewModel() as T
        } else if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GameViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class" + modelClass::class.simpleName)
    }
}