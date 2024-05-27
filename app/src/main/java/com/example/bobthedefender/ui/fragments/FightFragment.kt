package com.example.bobthedefender.ui.fragments

import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.AbsoluteLayout.LayoutParams
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.bobthedefender.R
import com.example.bobthedefender.databinding.EnemyBinding
import com.example.bobthedefender.databinding.FightDialogBinding
import com.example.bobthedefender.databinding.FragmentFightBinding
import com.example.bobthedefender.databinding.HeartBinding
import com.example.bobthedefender.ui.helpers.SharedPrefsManager
import com.example.bobthedefender.ui.models.Enemy
import com.example.bobthedefender.ui.models.FightState
import com.example.bobthedefender.ui.viewmodels.FightViewModel
import com.example.bobthedefender.ui.viewmodels.GameViewModel
import com.example.bobthedefender.ui.viewmodels.ViewModelFactory


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

    private lateinit var fireAnimator: ObjectAnimator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val displayMetrics = resources.displayMetrics
        displayWidth = displayMetrics.widthPixels
        displayHeight = displayMetrics.heightPixels
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val viewModelProvider = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(
                context.getSharedPreferences(
                    SharedPrefsManager.GAME_PREFERENCES,
                    Context.MODE_PRIVATE
                )
            )
        )
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
            showAlert(it)
            when (it) {
                FightState.LOSE -> {
                    clearField()
                }

                FightState.WIN -> {
                    gameViewModel.saveCoins(fightViewModel.coins)
                }

                else -> {}
            }
        }

        fightViewModel.enemiesLeft.observe(viewLifecycleOwner) {
            binding.enemiesLeft.text = it.toString()
        }

        fightViewModel.health.observe(viewLifecycleOwner) {
            binding.healthBar.removeAllViews()
            for (i in 0 until it) {
                val heartBinding = HeartBinding.inflate(LayoutInflater.from(context))
                binding.healthBar.addView(heartBinding.root)
            }
        }

        fightViewModel.enemies.observe(viewLifecycleOwner) {
            spawnEnemies(it)
        }

        binding.pauseButton.setOnClickListener {
            fightViewModel.changePauseState()
            changeAnimationsState()
        }

        Glide.with(requireContext())
            .asGif()
            .load(R.raw.bob_x256)
            .into(binding.player)

        binding.fire.alpha = 0f
        fireAnimator = ObjectAnimator.ofFloat(binding.fire, "alpha", 0f, 1f, 0f)
        fireAnimator.duration = 100
        fireAnimator.interpolator = LinearInterpolator()

    }

    private fun showAlert(fightState: FightState) {
        val builder = AlertDialog.Builder(requireContext())
        val dialogBinding =
            FightDialogBinding.inflate(LayoutInflater.from(requireContext()))
        builder.setView(dialogBinding.root)
        builder.setCancelable(false)
        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        when (fightState) {
            FightState.PAUSED -> {
                dialogBinding.dialogText.text = resources.getText(R.string.game_paused)
                dialogBinding.dialogButton.text = resources.getText(R.string.resume)
                dialogBinding.dialogButton.setOnClickListener {
                    fightViewModel.changePauseState()
                    changeAnimationsState()
                    dialog.dismiss()
                }
                dialog.show()
            }

            FightState.WIN -> {
                dialogBinding.dialogText.text = resources.getText(R.string.fight_won)
                dialogBinding.dialogButton.text = resources.getText(R.string.go_to_lobby)
                dialogBinding.dialogButton.setOnClickListener {
                    dialog.dismiss()
                    findNavController().navigate(R.id.action_gameScreenFragment_to_startFragment)
                }
                dialog.show()
            }

            FightState.LOSE -> {
                dialogBinding.dialogText.text = resources.getText(R.string.fight_lost)
                dialogBinding.dialogButton.text = resources.getText(R.string.go_to_lobby)
                dialogBinding.dialogButton.setOnClickListener {
                    dialog.dismiss()
                    findNavController().navigate(R.id.action_gameScreenFragment_to_startFragment)
                }
                dialog.show()
            }

            else -> {}
        }
    }

    private fun changeAnimationsState() {
        for ((_, enemyAnimator) in enemiesMap.values) {
            if (enemyAnimator.isPaused) {
                enemyAnimator.resume()
            } else {
                enemyAnimator.pause()
            }
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
                Glide.with(requireContext())
                    .asGif()
                    .load(R.raw.alien_192x192)
                    .into(enemyBinding.enemyBody)
                enemyBinding.root.layoutParams = LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT,
                    enemy.x,
                    enemy.y
                )
                binding.gameFieldContainer.addView(enemyBinding.root)
                enemy.health.observe(viewLifecycleOwner) {
                    val layoutParams = enemyBinding.enemyHealth.layoutParams
                    layoutParams.width = it * 10
                    enemyBinding.enemyHealth.layoutParams = layoutParams
                }
                val enemyAnimator =
                    ObjectAnimator.ofFloat(enemyBinding.root, View.TRANSLATION_X, -2150f)
                enemyAnimator.interpolator = LinearInterpolator()
                enemyAnimator.addUpdateListener { animation ->
                    val animatedValue = animation.animatedValue as Float
                    if (animatedValue == -2150f) {
                        fightViewModel.damagePlayer(enemy)
                        enemiesMap.remove(enemy)
                        binding.gameFieldContainer.removeView(enemyBinding.root)
                    }
                }
                enemyAnimator.duration = 10000
                enemyAnimator.start()

                enemyBinding.root.setOnClickListener {
                    if (fireAnimator.isRunning) {
                        fireAnimator.cancel()
                    }
                    fireAnimator.start()
                    if (fightViewModel.hitEnemy(enemy, gameViewModel.playersDamage)) {
                        binding.gameFieldContainer.removeView(enemyBinding.root)
                        enemyAnimator.cancel()
                        enemiesMap.remove(enemy)
                    }
                }

                enemiesMap[enemy] = Pair(enemyBinding.root, enemyAnimator)
            }
        }
    }
}