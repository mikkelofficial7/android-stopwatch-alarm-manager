package com.example.simplealarmmanager.Service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class StopServiceReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val serviceIntent = Intent(context, MyService::class.java)
        context.stopService(serviceIntent)
    }
}