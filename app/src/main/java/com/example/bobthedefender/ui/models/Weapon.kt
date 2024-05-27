package com.example.bobthedefender.ui.models

import com.google.gson.Gson

data class Weapon(
    val name: String,
    val damage: Int,
    val cost: Int = 0,
    val imageId: Int = 0
) {
    fun toJson(): String {
        return Gson().toJson(this)
    }

    companion object {
        fun fromJson(json: String): Weapon {
            return Gson().fromJson(json, Weapon::class.java)
        }
    }
}