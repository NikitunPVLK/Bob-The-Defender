package com.example.bobthedefender.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bobthedefender.ui.models.Weapon

class GameViewModel : ViewModel() {
    private val TAG = "GameViewModel"

    private val _coins = MutableLiveData(0)
    val coins: LiveData<Int>
        get() = _coins

    val catalog = mutableListOf(
        Weapon(
            "Name 1",
            1,
            100
        ),
        Weapon(
            "Name 1",
            2,
            200
        ),
        Weapon(
            "Name 1",
            3,
            300
        )
    )

    init {
        Log.d(TAG, hashCode().toString())
    }

    fun addCoins(coins: Int) {
        _coins.value = _coins.value!!.plus(coins)
    }
}