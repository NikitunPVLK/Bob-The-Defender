package com.example.bobthedefender.ui.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bobthedefender.ui.models.Weapon

class GameViewModel(private val sharedPreferences: SharedPreferences) : ViewModel() {
    private val TAG = "GameViewModel"

    private val _coins: MutableLiveData<Int>
        get() = MutableLiveData(sharedPreferences.getInt("coins", 0))
    val coins: LiveData<Int>
        get() = _coins

    val catalog = mutableListOf(
        Weapon(
            "Name 2",
            2,
            10
        ),
        Weapon(
            "Name 3",
            3,
            10
        ),
        Weapon(
            "Name 4",
            6,
            10
        )
    )

    private var currentWeapon: Weapon = Weapon("Name 1", 1, 0)

    val playersDamage: Int
        get() = currentWeapon.damage

    init {
        Log.d(TAG, hashCode().toString())
    }

    fun saveCoins(amount: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt("coins", amount)
        editor.apply()
    }

    fun onItemBought(weapon: Weapon) {
        currentWeapon = weapon
        _coins.value = _coins.value!!.minus(weapon.cost)
        Log.d(TAG, "$currentWeapon")
    }
}