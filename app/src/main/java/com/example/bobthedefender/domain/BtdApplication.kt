package com.example.bobthedefender.domain

import android.app.Application
import com.example.bobthedefender.ui.helpers.SharedPrefsManager
import com.example.bobthedefender.ui.models.Player
import com.example.bobthedefender.ui.models.Weapon

class BtdApplication : Application() {
    val player by lazy {
        Player(
            SharedPrefsManager.getCoins(
                applicationContext.getSharedPreferences(SharedPrefsManager.GAME_PREFERENCES, MODE_PRIVATE)
            ),
            SharedPrefsManager.getWeapon(
                applicationContext.getSharedPreferences(SharedPrefsManager.GAME_PREFERENCES, MODE_PRIVATE)
            ) ?: Weapon("Pistol", 1, true)
        )
    }
}