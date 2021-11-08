package com.example.simplealarmmanager.AlarmManager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.widget.Toast
import com.example.simplealarmmanager.MainActivity


class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "123")
        wl.acquire()

        // Put here YOUR code.
        (context as MainActivity).showToast("Alarm Ringing!!")

        wl.release()
    }

    fun setAlarm(context: Context) {
        (context as MainActivity).showToast("Alarm Started")

        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val i = Intent(context, AlarmReceiver::class.java)
        val pi = PendingIntent.getBroadcast(context, 0, i, 0)

        am.setRepeating(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis(),
            (1).toLong(),
            pi
        )
    }

    fun cancelAlarm(context: Context) {
        (context as MainActivity).showToast("Alarm Cancelled")

        val intent = Intent(context, AlarmReceiver::class.java)
        val sender = PendingIntent.getBroadcast(context, 0, intent, 0)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(sender)
    }
}