package com.example.bobthedefender.ui.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bobthedefender.ui.helpers.SharedPrefsManager
import com.example.bobthedefender.ui.models.Enemy
import com.example.bobthedefender.ui.models.FightState
import com.example.bobthedefender.ui.models.Player
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class FightViewModel(
    private val sharedPreferences: SharedPreferences,
    private val player: Player
) : ViewModel() {
    private val _health = MutableLiveData(3)
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

    private var enemiesKilled = 0

    private val _enemiesLeft = MutableLiveData(enemiesToKill)
    val enemiesLeft: LiveData<Int>
        get() = _enemiesLeft

    private lateinit var spawnJob: Job

    var earnedCoins: Int = 0

    fun startGame() {
        _health.value = 3
        enemiesToKill = 10
        earnedCoins = 0
        enemiesKilled = 0
        _enemiesLeft.value = enemiesToKill
        _fightState.value = FightState.IN_PROGRESS
        spawnEnemies()
    }

    fun damagePlayer(enemy: Enemy) {
        _enemiesLeft.value = _enemiesLeft.value?.minus(1)
        innerList.remove(enemy)
        _health.value = _health.value!! - 1
        if (_health.value!! <= 0) {
            stopGame()
            _fightState.value = FightState.LOSE
        } else if (enemiesLeft.value!! <= 0 && innerList.isEmpty()) {
            win()
        }
    }

    private fun win() {
        stopGame()
        player.coins += earnedCoins
        SharedPrefsManager.saveCoins(player.coins, sharedPreferences)
        SharedPrefsManager.addToTotalKills(enemiesKilled, sharedPreferences)
        SharedPrefsManager.addToTotalCoins(earnedCoins, sharedPreferences)
        _fightState.value = FightState.WIN
    }

    fun hitEnemy(enemy: Enemy): Boolean {
        SharedPrefsManager.addToTotalShots(1, sharedPreferences)
        if (enemy.receiveDamage(player.weapon.damage)) {
            innerList.remove(enemy)
            _enemies.postValue(innerList)
            _enemiesLeft.value = _enemiesLeft.value!!.minus(1)
            earnedCoins += enemy.initialHealth
            enemiesKilled += 1
            if (_enemiesLeft.value!! <= 0) {
                win()
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
                    val enemy = Enemy(Random.nextInt(1, 10))
                    innerList.add(enemy)
                    _enemies.postValue(innerList)
                    counter += 1
                    delay(1000)
                } else if (fightState.value == FightState.PAUSED) {
                    delay(100)
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