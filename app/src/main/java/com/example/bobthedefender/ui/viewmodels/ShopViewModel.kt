package com.example.bobthedefender.ui.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bobthedefender.R
import com.example.bobthedefender.ui.helpers.SharedPrefsManager
import com.example.bobthedefender.ui.models.Player
import com.example.bobthedefender.ui.models.Weapon

class ShopViewModel(
    private val sharedPreferences: SharedPreferences,
    private val player: Player
) : ViewModel() {

    private val _coins = MutableLiveData(player.coins)
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

    private var _catalog: MutableLiveData<List<Weapon>> = MutableLiveData(weapons.map {
        if (it.name == player.weapon.name) player.weapon else
            it
    }.toMutableList())
    val catalog: LiveData<List<Weapon>>
        get() = _catalog

    fun buyWeapon(weapon: Weapon): Boolean {
        if (weapon.cost <= player.coins && !weapon.isBought) {
            weapon.isBought = true
            player.weapon = weapon
            player.coins -= weapon.cost
            _catalog.postValue(_catalog.value!!.toList())
            SharedPrefsManager.saveWeapon(weapon, sharedPreferences)
            _coins.postValue(player.coins)
            SharedPrefsManager.saveCoins(player.coins, sharedPreferences)
            return true
        }
        return false
    }

    fun isBuyEnabled(weapon: Weapon): Boolean {
        return weapon.cost <= player.coins && !weapon.isBought
    }
}