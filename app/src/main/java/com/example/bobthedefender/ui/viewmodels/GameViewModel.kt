package com.example.bobthedefender.ui.viewmodels

import android.content.SharedPreferences
import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bobthedefender.R
import com.example.bobthedefender.ui.models.Weapon

class GameViewModel(private val sharedPreferences: SharedPreferences) : ViewModel() {
    private val TAG = "GameViewModel"

    private val _coins = MutableLiveData(sharedPreferences.getInt("coins", 0))
    val coins: LiveData<Int>
        get() = _coins

    val catalog = mutableListOf(
        Weapon(
            "Old Rifle",
            2,
            20,
            R.drawable.old_rifle
        ),
        Weapon(
            "M16",
            4,
            40,
            R.drawable.m16
        ),
        Weapon(
            "BMG",
            6,
            60,
            R.drawable.bmg
        )
    )

    private var currentWeapon: Weapon = Weapon("Pistol", 1)

    val playersDamage: Int
        get() = currentWeapon.damage

    init {
        Log.d(TAG, hashCode().toString())
    }

    fun saveCoins(amount: Int) {
        val editor = sharedPreferences.edit()
        _coins.value = _coins.value!!.plus(amount)
        editor.putInt("coins", coins.value!!)
        editor.apply()
    }

    fun onItemBought(weapon: Weapon) {
        if (weapon.cost <= _coins.value!!) {
            currentWeapon = weapon
            _coins.value = _coins.value!!.minus(weapon.cost)
            val editor = sharedPreferences.edit()
            editor.putInt("coins", coins.value!!)
            editor.apply()
            Log.d(TAG, "$currentWeapon")
        }
    }
}