package com.example.simplealarmmanager.Service

import android.app.*
import android.content.Intent
import android.os.*
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.simplealarmmanager.MainActivity.Companion.ACTION_TIMER_DATA
import com.example.simplealarmmanager.MainActivity.Companion.TAG_ACTION_AUTO_STOP_SERVICE
import com.example.simplealarmmanager.MainActivity.Companion.TAG_CAN_SHOW_TIMER_DATA_FOREEGROUND
import com.example.simplealarmmanager.MainActivity.Companion.TAG_TIMER_DATA
import com.example.simplealarmmanager.NotificationBuilderTemplate
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MyService : Service() {
    private var canShowForeground = false

    private var timerData: String = ""

    private var currentDateTimerData: Date? = null

    private var handler = Handler(Looper.getMainLooper())

    private val binder = LocalBinder()

    private var runnable = object : Runnable {
        override fun run() {
            timerData = getCurrentTime()

            sendIntentMessage()

            if(canShowForeground) {
                NotificationBuilderTemplate.showNotification(
                    context = this@MyService,
                    title = timerData
                )
            }

            handler.postDelayed(this, 1000)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        val isAutoStopService = intent?.getBooleanExtra(TAG_ACTION_AUTO_STOP_SERVICE, false) == true
        canShowForeground = intent?.getBooleanExtra(TAG_CAN_SHOW_TIMER_DATA_FOREEGROUND, false) == true

        handler.post(runnable)

        if(isAutoStopService) stopSelf()

        return START_STICKY
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)

        handler.post(runnable)
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onRebind(intent: Intent?) {
        super.onRebind(intent)

        stopForeground(true)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return true
    }

    private fun getCurrentTime() : String {
        currentDateTimerData = Calendar.getInstance().time

        currentDateTimerData?.let { currentDateTimerData ->

            val hour = if(currentDateTimerData.hours < 10) "0${currentDateTimerData.hours}" else currentDateTimerData.hours
            val minute = if(currentDateTimerData.minutes < 10) "0${currentDateTimerData.minutes}" else currentDateTimerData.minutes
            val second = if(currentDateTimerData.seconds < 10) "0${currentDateTimerData.seconds}" else currentDateTimerData.seconds

            return "$hour : $minute : $second"

        } ?: kotlin.run {
            return ""
        }
    }

    private fun sendIntentMessage() {
        val intent = Intent(ACTION_TIMER_DATA)
        intent.putExtra(TAG_TIMER_DATA, timerData)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    inner class LocalBinder : Binder() {
        fun getService(): MyService = this@MyService
    }

}