package com.example.bobthedefender.ui.helpers

import android.content.SharedPreferences
import com.example.bobthedefender.ui.models.Weapon

object SharedPrefsManager {
    private const val WEAPON_KEY = "weapon"
    private const val COINS_KEY = "coins"
    private const val TOTAL_COINS_EARNED = "total_coins"
    private const val TOTAL_SHOTS = "total_shots"
    private const val TOTAL_KILLS = "total_kills"

    const val GAME_PREFERENCES = "game_preferences"

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

    fun getTotalCoins(sharedPreferences: SharedPreferences) : Int {
        return sharedPreferences.getInt(TOTAL_COINS_EARNED, 0)
    }

    fun addToTotalCoins(amount: Int, sharedPreferences: SharedPreferences) {
        sharedPreferences.edit()
            .putInt(TOTAL_COINS_EARNED, getTotalCoins(sharedPreferences) + amount)
            .apply()
    }

    fun getTotalKills(sharedPreferences: SharedPreferences) : Int {
        return sharedPreferences.getInt(TOTAL_KILLS, 0)
    }

    fun addToTotalKills(amount: Int, sharedPreferences: SharedPreferences) {
        sharedPreferences.edit()
            .putInt(TOTAL_KILLS, getTotalKills(sharedPreferences) + amount)
            .apply()
    }

    fun getTotalShots(sharedPreferences: SharedPreferences) : Int {
        return sharedPreferences.getInt(TOTAL_SHOTS, 0)
    }

    fun addToTotalShots(amount: Int, sharedPreferences: SharedPreferences) {
        sharedPreferences.edit()
            .putInt(TOTAL_SHOTS, getTotalShots(sharedPreferences) + amount)
            .apply()
    }
}