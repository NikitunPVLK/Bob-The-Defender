package com.example.bobthedefender.ui.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

data class Enemy(
    val x: Int,
    val y: Int,
    val width: Int,
    val height: Int,
    val initialHealth: Int = 3
) {
    private val _health = MutableLiveData(initialHealth)
    val health: LiveData<Int>
        get() = _health
    fun receiveDamage(damage: Int) : Boolean {
        _health.value = _health.value?.minus(damage)
        return _health.value!! <= 0
    }
}
