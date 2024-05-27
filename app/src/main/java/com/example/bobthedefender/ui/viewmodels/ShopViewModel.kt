package com.example.bobthedefender.ui.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bobthedefender.R
import com.example.bobthedefender.ui.helpers.SharedPrefsManager
import com.example.bobthedefender.ui.models.Weapon

class ShopViewModel(private val sharedPreferences: SharedPreferences) : ViewModel() {

    private val _coins = MutableLiveData(SharedPrefsManager.getCoins(sharedPreferences))
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

    fun saveCoins(amount: Int) {
        _coins.value = _coins.value!!.plus(amount)
        SharedPrefsManager.saveCoins(_coins.value!!, sharedPreferences)
    }

    fun onItemBought(weapon: Weapon): Boolean {
        if (weapon.cost <= _coins.value!!) {
            SharedPrefsManager.saveWeapon(weapon, sharedPreferences)
            _coins.value = _coins.value!!.minus(weapon.cost)
            SharedPrefsManager.saveCoins(_coins.value!!, sharedPreferences)
            return true
        }
        return false
    }

    fun isBuyEnabled(weapon: Weapon): Boolean {
        return weapon.cost <= _coins.value!!
    }
}