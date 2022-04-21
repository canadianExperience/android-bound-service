package com.me.android_bound_service

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import com.me.android_bound_service.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var myBoundService: MyBoundService
    private var isServiceBound = false

    private val serviceConnection = object : ServiceConnection{
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val myBinder = service as MyBoundService.MyBinder
            myBoundService = myBinder.getService()
            isServiceBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isServiceBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.printTimestamp.setOnClickListener {
            if(isServiceBound){
                binding.timestampText.text = myBoundService.getTimestamp()
            }
        }

        binding.stopService.setOnClickListener {
            if (isServiceBound) {
                unbindService(serviceConnection)
                isServiceBound = false
            }
            val intent = Intent(this@MainActivity, MyBoundService::class.java)
            stopService(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        val intent = Intent(this, MyBoundService::class.java)
        startService(intent)
        bindService(intent, serviceConnection, BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        if (isServiceBound) {
            unbindService(serviceConnection)
            isServiceBound = false
        }
    }


}