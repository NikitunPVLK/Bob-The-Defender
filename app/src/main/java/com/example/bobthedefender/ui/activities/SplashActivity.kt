package com.example.bobthedefender.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Window
import android.view.WindowManager
import com.example.bobthedefender.databinding.ActivitySplashBinding

class SplashActivity : Activity() {

    private var _binding: ActivitySplashBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        _binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val progressBar = binding.progressBar
        progressBar.max = 3500

        object : CountDownTimer(3500, 100) {
            override fun onTick(millisUntilFinished: Long) {
                progressBar.setProgress(progressBar.progress + 100, true)
                binding.progressPercents.text =
                    "${((progressBar.progress.toFloat() / 3500f * 100).toInt())}%"
            }

            override fun onFinish() {
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.start()
    }
}