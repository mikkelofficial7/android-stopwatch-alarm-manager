package com.example.simplealarmmanager.AlarmManager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class AutoStartAlarmReceiver : BroadcastReceiver() {
    // Alarm will auto start when device boot complete

    var alarm = AlarmReceiver()

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            alarm.setAlarm(context)
        }
    }
}