package com.me.android_bound_service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.os.SystemClock
import android.util.Log
import android.widget.Chronometer

class MyBoundService: Service() {

    private lateinit var chronometer: Chronometer
    private val myBinder = MyBinder()

    override fun onCreate() {
        super.onCreate()
        Log.e("SERVICE", "Bound Service in onCreate")
        chronometer = Chronometer(this).apply {
            base = SystemClock.elapsedRealtime()
            start()
        }

    }
    override fun onBind(intent: Intent?): IBinder? {
        Log.e("SERVICE", "Bound Service in onBind")
        return myBinder
    }

    override fun onRebind(intent: Intent?) {
        Log.e("SERVICE", "Bound Service in onRebind")
        super.onRebind(intent)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.e("SERVICE", "Bound Service in onUnbind")
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("SERVICE", "Bound Service in onDestroy")
        chronometer.stop()
    }

    fun getTimestamp(): String {
        val elapsedMillis: Long = (SystemClock.elapsedRealtime() - chronometer.base)
        val hours = (elapsedMillis / 3600000).toInt()
        val minutes = (elapsedMillis - hours * 3600000).toInt() / 60000
        val seconds = (elapsedMillis - hours * 3600000 - minutes * 60000).toInt() / 1000
        val millis = (elapsedMillis - hours * 3600000 - minutes * 60000 - seconds * 1000).toInt()
        return "$hours:$minutes:$seconds:$millis"
    }

    inner class MyBinder : Binder() {
        fun getService() = this@MyBoundService
    }
}

