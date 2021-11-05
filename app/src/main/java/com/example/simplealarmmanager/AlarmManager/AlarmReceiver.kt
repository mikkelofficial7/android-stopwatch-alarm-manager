package com.example.simplealarmmanager.AlarmManager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.widget.Toast


class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "123")
        wl.acquire()

        // Put here YOUR code.
        Toast.makeText(context, "Alarm at ", Toast.LENGTH_LONG).show() // For example

        wl.release()
    }

    fun setAlarm(context: Context) {
        Toast.makeText(context, "Alarm Started", Toast.LENGTH_SHORT).show()

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
        Toast.makeText(context, "Alarm Cancelled", Toast.LENGTH_SHORT).show()

        val intent = Intent(context, AlarmReceiver::class.java)
        val sender = PendingIntent.getBroadcast(context, 0, intent, 0)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(sender)
    }
}