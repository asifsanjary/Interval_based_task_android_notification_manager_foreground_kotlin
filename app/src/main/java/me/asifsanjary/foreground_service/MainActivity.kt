package me.asifsanjary.foreground_service

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import me.asifsanjary.foreground_service.databinding.ActivityMainBinding

class MainActivity : Activity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("MainActivity", "[Start] Interval-Based Service")

        Intent(this, IntervalBasedService::class.java).also { intent ->
            applicationContext.startForegroundService(intent)
        }
    }
}