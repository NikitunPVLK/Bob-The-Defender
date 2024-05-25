package com.example.bobthedefender.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bobthedefender.ui.models.Enemy
import com.example.bobthedefender.ui.models.FightState
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
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

    private var enemiesToKill = 10

    private val _enemiesLeft = MutableLiveData(enemiesToKill)
    val enemiesLeft: LiveData<Int>
        get() = _enemiesLeft

    private lateinit var spawnJob: Job

    fun startGame() {
        _health.value = 10
        enemiesToKill = 10
        _enemiesLeft.value = enemiesToKill
        _fightState.value = FightState.IN_PROGRESS
        spawnEnemies()
    }

    fun dealDamage() {
        _health.value = _health.value!! - 1
        if (_health.value!! <= 0) {
            stopGame()
            _fightState.value = FightState.LOSE
        }
        _enemiesLeft.value = _enemiesLeft.value?.minus(1)
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
        var counter = 0
        spawnJob = viewModelScope.launch {
            while (counter < enemiesToKill) {
                if (fightState.value == FightState.IN_PROGRESS) {
                    val x = 2000
                    val y = Random.nextInt(0, 800)
                    val enemy = Enemy(x, y, 160, 180)
                    innerList.add(enemy)
                    _enemies.postValue(innerList)
                    counter += 1
                    delay(1000)
                    Log.d("FightViewModel", "${enemies.value?.size}")
                } else if (fightState.value == FightState.PAUSED) {
                    // Wait here while spawning is paused
                    delay(100) // Adjust delay as needed to avoid busy waiting
                }
            }
        }
    }

    fun changePauseState() {
        if (fightState.value == FightState.IN_PROGRESS) {
            _fightState.value = FightState.PAUSED
        } else if (fightState.value == FightState.PAUSED) {
            _fightState.value = FightState.IN_PROGRESS
        }
    }
}