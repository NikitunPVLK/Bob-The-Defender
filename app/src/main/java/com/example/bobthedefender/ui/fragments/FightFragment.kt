package com.example.bobthedefender.ui.fragments

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsoluteLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.bobthedefender.R
import com.example.bobthedefender.databinding.EnemyBinding
import com.example.bobthedefender.databinding.FragmentFightBinding
import com.example.bobthedefender.ui.viewmodels.FightViewModel
import com.example.bobthedefender.ui.models.Enemy
import com.example.bobthedefender.ui.models.FightState
import com.example.bobthedefender.ui.viewmodels.GameViewModel

class FightFragment : Fragment() {
    private val TAG = "FightFragment"

    private var _binding: FragmentFightBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var fightViewModel: FightViewModel
    private lateinit var gameViewModel: GameViewModel

    private val enemiesMap = mutableMapOf<Enemy, Pair<View, ObjectAnimator>>()

    private var displayWidth: Int = 0
    private var displayHeight: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val displayMetrics = resources.displayMetrics
        displayWidth = displayMetrics.widthPixels
        displayHeight = displayMetrics.heightPixels
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val viewModelProvider = ViewModelProvider(requireActivity())
        fightViewModel = viewModelProvider[FightViewModel::class.java]
        gameViewModel = viewModelProvider[GameViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "creation")
        _binding = FragmentFightBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fightViewModel.startGame()

        fightViewModel.fightState.observe(viewLifecycleOwner) {
            when (it) {
                FightState.LOSE -> {
                    clearField()
                    findNavController().navigate(R.id.action_gameScreenFragment_to_startFragment)
                }

                FightState.WIN -> {
                    findNavController().navigate(R.id.action_gameScreenFragment_to_startFragment)
                }

                else -> {}
            }
        }

        fightViewModel.enemiesLeft.observe(viewLifecycleOwner) {
            binding.enemiesLeft.text = it.toString()
        }

        fightViewModel.health.observe(viewLifecycleOwner) {
            binding.health.text = it.toString()
        }

        fightViewModel.enemies.observe(viewLifecycleOwner) {
            spawnEnemies(it)
        }
    }

    private fun clearField() {
        for ((enemyView, enemyAnimator) in enemiesMap.values) {
            binding.gameFieldContainer.removeView(enemyView)
            enemyAnimator.cancel()
        }
        enemiesMap.clear()
    }

    private fun spawnEnemies(enemies: List<Enemy>) {
        for (enemy in enemies) {
            if (!enemiesMap.contains(enemy)) {
                val enemyBinding = EnemyBinding.inflate(LayoutInflater.from(context))
                enemyBinding.root.layoutParams = AbsoluteLayout.LayoutParams(
                    enemy.width,
                    enemy.height,
                    enemy.x,
                    enemy.y
                )
                Glide.with(requireContext())
                    .asGif()
                    .load(R.raw.alien)
                    .into(enemyBinding.enemyBody)
                binding.gameFieldContainer.addView(enemyBinding.root)
                enemy.health.observe(viewLifecycleOwner) {
                    enemyBinding.enemyHp.text =
                        enemy.health.value.toString()
                }
                val enemyAnimator = ObjectAnimator.ofFloat(enemyBinding.root, View.TRANSLATION_X, -2400f)
                enemyAnimator.addUpdateListener { animation ->
                    val animatedValue = animation.animatedValue as Float
                    if (animatedValue == -2400f) {
                        fightViewModel.dealDamage()
                        enemiesMap.remove(enemy)
                        binding.gameFieldContainer.removeView(enemyBinding.root)
                    }
                }
                enemyAnimator.duration = 10000
                enemyAnimator.start()

                enemyBinding.root.setOnClickListener {
                    if (fightViewModel.hitEnemy(enemy, gameViewModel.playersDamage)) {
                        binding.gameFieldContainer.removeView(enemyBinding.root)
                        enemyAnimator.cancel()
                        enemiesMap.remove(enemy)
                        gameViewModel.addCoins(1)
                    }
                }

                enemiesMap[enemy] = Pair(enemyBinding.root, enemyAnimator)
            }
        }
    }
}