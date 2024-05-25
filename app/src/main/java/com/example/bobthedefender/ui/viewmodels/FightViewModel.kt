package com.example.bobthedefender.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bobthedefender.ui.models.Enemy
import com.example.bobthedefender.ui.models.FightState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.random.Random

class FightViewModel : ViewModel() {
    private val _health = MutableLiveData(0)
    val health: LiveData<Int>
        get() = _health

    private val innerList: MutableList<Enemy> = mutableListOf()

    private val _enemies: MutableLiveData<List<Enemy>> = MutableLiveData(listOf())
    val enemies: LiveData<List<Enemy>>
        get() = _enemies

    private val _fightState = MutableLiveData(FightState.NOT_STARTED)
    val fightState: LiveData<FightState>
        get() = _fightState

    private val weaponDamage: Int = 1

    private var enemiesToKill = 10

    private val _enemiesLeft = MutableLiveData(enemiesToKill)
    val enemiesLeft: LiveData<Int>
        get() = _enemiesLeft

    private lateinit var spawnJob: Job

    fun startGame() {
        _health.value = 10
        enemiesToKill = 10
        _enemiesLeft.value = enemiesToKill
        spawnEnemies()
        _fightState.value = FightState.IN_PROGRESS
    }

    fun dealDamage() {
        _health.value = _health.value!! - 1
        if (_health.value!! <= 0) {
            stopGame()
            _fightState.value = FightState.LOSE
        }
    }

    fun hitEnemy(enemy: Enemy, damage: Int): Boolean {
        if (enemy.receiveDamage(damage)) {
            innerList.remove(enemy)
            _enemies.postValue(innerList)
            _enemiesLeft.value = _enemiesLeft.value!!.minus(1)
            if (_enemiesLeft.value!! <= 0) {
                stopGame()
                _fightState.value = FightState.WIN
            }
            return true
        }
        return false
    }

    private fun stopGame() {
        spawnJob.cancel()
        innerList.clear()
        _enemies.postValue(innerList)
    }

    private fun spawnEnemies() {
        spawnJob = viewModelScope.launch {
            for (i in 0 until enemiesToKill) {
                if (isActive) {
                    val x = 2000
                    val y = Random.nextInt(0, 800)
                    val enemy = Enemy(x, y, 160, 180)
                    innerList.add(enemy)
                    _enemies.postValue(innerList)
                    delay(1000)
                    Log.d("FightViewModel", "${enemies.value?.size}")
                }
            }
        }
    }
}