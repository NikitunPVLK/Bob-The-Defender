package com.example.bobthedefender.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bobthedefender.ui.models.Enemy
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.random.Random

class GameViewModel : ViewModel() {
    private val _health = MutableLiveData(0)
    val health: LiveData<Int>
        get() = _health

    private var _points = MutableLiveData(0)
    val points: LiveData<Int>
        get() = _points

    private val innerList: MutableList<Enemy> = mutableListOf()

    private val _enemies: MutableLiveData<List<Enemy>> = MutableLiveData(listOf())
    val enemies: LiveData<List<Enemy>>
        get() = _enemies

    private val _isGameLost = MutableLiveData(false)
    val isGameLost: LiveData<Boolean>
        get() = _isGameLost

    private lateinit var spawnJob: Job

    fun startGame() {
        _health.value = 10
        _points.value = 0
        spawnEnemies()
        _isGameLost.value = false
    }

    fun dealDamage() {
        _health.value = _health.value!! - 1
        if (_health.value!! <= 0) {
            stopGame()
        }
    }

    fun hitEnemy(enemy: Enemy): Boolean {
        _points.value = _points.value!! + 1
        innerList.remove(enemy)
        _enemies.postValue(innerList)
        return true
    }

    private fun stopGame() {
        spawnJob.cancel()
        innerList.clear()
        _enemies.postValue(innerList)
        _isGameLost.value = true
    }

    private fun spawnEnemies() {
        spawnJob = viewModelScope.launch {
            while (isActive) {
                val x = 2000
                val y = Random.nextInt(0, 800)
                val enemy = Enemy(x, y, 120, 180)
                innerList.add(enemy)
                _enemies.postValue(innerList)
                delay(1000)
                Log.d("GameViewModel", "${enemies.value?.size}")
            }
        }
    }
}