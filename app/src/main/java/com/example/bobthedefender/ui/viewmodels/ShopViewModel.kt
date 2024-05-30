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

    private val weapons = mutableListOf(
        Weapon(
            "Old Rifle",
            2,
            false,
            20,
            R.drawable.old_rifle
        ),
        Weapon(
            "M16",
            4,
            false,
            40,
            R.drawable.m16
        ),
        Weapon(
            "BMG",
            6,
            false,
            60,
            R.drawable.bmg
        )
    )

    private var _catalog: MutableLiveData<List<Weapon>>
    val catalog: LiveData<List<Weapon>>
        get() = _catalog

    init {
        val playersWeapon = SharedPrefsManager.getWeapon(sharedPreferences)
        _catalog = MutableLiveData(weapons.map {
            if (it.name == playersWeapon?.name) playersWeapon else
                it
        }.toMutableList())
    }

    fun saveCoins(amount: Int) {
        _coins.value = _coins.value!!.plus(amount)
        SharedPrefsManager.saveCoins(_coins.value!!, sharedPreferences)
    }

    fun onItemBought(weapon: Weapon): Boolean {
        if (weapon.cost <= _coins.value!! && !weapon.isBought) {
            weapon.isBought = true
            _catalog.postValue(_catalog.value!!.toList())
            SharedPrefsManager.saveWeapon(weapon, sharedPreferences)
            _coins.value = _coins.value!!.minus(weapon.cost)
            SharedPrefsManager.saveCoins(_coins.value!!, sharedPreferences)
            return true
        }
        return false
    }

    fun isBuyEnabled(weapon: Weapon): Boolean {
        return weapon.cost <= _coins.value!! && !weapon.isBought
    }
}