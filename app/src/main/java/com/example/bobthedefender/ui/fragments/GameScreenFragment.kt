package com.example.bobthedefender.ui.fragments

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsoluteLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.bobthedefender.R
import com.example.bobthedefender.databinding.FragmentGameScreenBinding
import com.example.bobthedefender.ui.GameViewModel
import com.example.bobthedefender.ui.models.Enemy

class GameScreenFragment : Fragment() {
    private val TAG = "GameScreenFragment"

    private var _binding: FragmentGameScreenBinding? = null
    private val binding
        get() = _binding!!

    private val gameViewModel: GameViewModel by viewModels()

    private val enemiesMap = mutableMapOf<Enemy, Pair<View, ObjectAnimator>>()

    private var displayWidth: Int = 0
    private var displayHeight: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val displayMetrics = resources.displayMetrics
        displayWidth = displayMetrics.widthPixels
        displayHeight = displayMetrics.heightPixels
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "creation")
        _binding = FragmentGameScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gameViewModel.startGame()

        gameViewModel.isGameLost.observe(viewLifecycleOwner) {
            if (it) {
                clearField()
            }
        }

        gameViewModel.health.observe(viewLifecycleOwner) {
            binding.health.text = it.toString()
        }

        gameViewModel.points.observe(viewLifecycleOwner) {
            binding.points.text = it.toString()
        }

        gameViewModel.enemies.observe(viewLifecycleOwner) {
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
                val inflater = LayoutInflater.from(context)
                val enemyView = inflater.inflate(R.layout.enemy, null)
                enemyView.layoutParams = AbsoluteLayout.LayoutParams(
                    enemy.width,
                    enemy.height,
                    enemy.x,
                    enemy.y
                )
                val enemyBody = enemyView.findViewById<ImageView>(R.id.enemy_body)
                Glide.with(requireContext())
                    .asGif()
                    .load(R.raw.alien)
                    .into(enemyBody)
                binding.gameFieldContainer.addView(enemyView)
                enemy.health.observe(viewLifecycleOwner) {
                    enemyView.findViewById<TextView>(R.id.enemy_hp).text = enemy.health.value.toString()
                }
                val enemyAnimator = ObjectAnimator.ofFloat(enemyView, View.TRANSLATION_X, -2400f)
                enemyAnimator.addUpdateListener { animation ->
                    val animatedValue = animation.animatedValue as Float
                    if (animatedValue == -2400f) {
                        gameViewModel.dealDamage()
                        enemiesMap.remove(enemy)
                        binding.gameFieldContainer.removeView(enemyView)
                    }
                }
                enemyAnimator.duration = 10000
                enemyAnimator.start()

                enemyView.setOnClickListener {
                    if (gameViewModel.hitEnemy(enemy)) {
                        binding.gameFieldContainer.removeView(enemyView)
                        enemyAnimator.cancel()
                        enemiesMap.remove(enemy)
                    }
                }

                enemiesMap[enemy] = Pair(enemyView, enemyAnimator)
            }
        }
    }
}