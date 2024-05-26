package com.example.bobthedefender.ui.helpers

import android.content.SharedPreferences
import com.example.bobthedefender.ui.models.Weapon

object SharedPrefsManager {
    private const val WEAPON_KEY = "weapon"
    private const val COINS_KEY = "coins"

    fun saveWeapon(weapon: Weapon, sharedPreferences: SharedPreferences) {
        sharedPreferences.edit()
            .putString(WEAPON_KEY, weapon.toJson())
            .apply()
    }

    fun getWeapon(sharedPreferences: SharedPreferences): Weapon? {
        val weaponJson = sharedPreferences.getString(WEAPON_KEY, null)
        return weaponJson?.let { Weapon.fromJson(it) }
    }

    fun saveCoins(amount: Int, sharedPreferences: SharedPreferences) {
        sharedPreferences.edit()
            .putInt(COINS_KEY, amount)
            .apply()
    }

    fun getCoins(sharedPreferences: SharedPreferences): Int {
        return sharedPreferences.getInt(COINS_KEY, 0)
    }
}