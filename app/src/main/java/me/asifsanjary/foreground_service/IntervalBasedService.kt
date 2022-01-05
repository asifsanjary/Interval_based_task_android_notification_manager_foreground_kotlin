package me.asifsanjary.foreground_service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi

class IntervalBasedService : Service() {

    private var timer: CountDownTimer? = null
    private var isTimerStopped: Boolean = false


    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d(TAG, "[Started] Interval-Based Service")

        startForeground(NOTIFICATION_ID, createNotification())

        startIntervalBasedWork(this, startId)
        // If we get killed, after returning from here, restart
        return START_STICKY
    }

    private fun startIntervalBasedWork(service: Service, serviceId: Int) {
        // Implement your own CountDownTimer for better usability

        timer = object : CountDownTimer(TOTAL_TIME_REQUIRED_MLS, TIME_INTERVAL_MLS) {
            override fun onTick(millisUntilFinished: Long) {
                Log.d(TAG, "${millisUntilFinished / 1000} seconds remaining")
                // TODO: Do timer based work
            }

            override fun onFinish() {
                Log.d(TAG, "Timer Finished")
                isTimerStopped = true

                Log.d(TAG, "Service Stopping")
                service.let {
                    it.stopForeground(true)
                    it.stopSelf(serviceId)
                }
            }

        }

        isTimerStopped = false
        (timer as CountDownTimer).start()
    }

    private fun createNotification(): Notification {
        Log.d(TAG, "Creating Notification")
        createNotificationChannel()
        return Notification.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(NOTIFICATION_TITLE)
            .setContentText(NOTIFICATION_MESSAGE)
            .setSmallIcon(android.R.color.transparent)
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
            .createNotificationChannel(
                NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_TITLE, NotificationManager.IMPORTANCE_HIGH
                )
            )
    }

    override fun onBind(intent: Intent): IBinder? {
        // We don't provide binding, so return null
        return null
    }

    override fun onDestroy() {
        if (!isTimerStopped) (timer as CountDownTimer).cancel()
        Log.d(TAG, "Service Stop")
    }

    companion object {
        private const val NOTIFICATION_ID = 56
        private const val NOTIFICATION_CHANNEL_ID =
            "INTERVAL_BASED_NOTIFICATION_CHANNEL_$NOTIFICATION_ID"
        private const val NOTIFICATION_TITLE = "Interval-Based Notification"
        private const val NOTIFICATION_MESSAGE = "Some Work In Progress"
        private const val TOTAL_TIME_REQUIRED_MLS: Long = 120_000
        private const val TIME_INTERVAL_MLS: Long =
            10_000 //If interval is greater-equal 15 minutes, try WorkManager
        private const val TAG = "IntervalBasedService"
    }
}