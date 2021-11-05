package com.example.simplealarmmanager

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.simplealarmmanager.Service.MyService
import com.example.simplealarmmanager.Service.StopServiceReceiver

object NotificationBuilderTemplate {
    private var notificationBuilder: NotificationCompat.Builder? = null

    private lateinit var notificationManager: NotificationManager

    private var NotificationId = 101

    private var defaultMessage = "Tap below to stop service"

    fun showNotification(context: MyService, title: String, message: String = defaultMessage) {
        notificationManager = context.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            @SuppressLint("WrongConstant")
            val notificationChannel = NotificationChannel(
                MainActivity.NOTIFICATION_CHANNEL_ID,
                context.resources.getString(R.string.app_name),
                NotificationManager.IMPORTANCE_MAX
            )

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = context.resources.getColor(R.color.teal_200)
            notificationChannel.vibrationPattern = longArrayOf(1000, 1000)
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        if (notificationBuilder == null) {
            notificationBuilder = NotificationCompat.Builder(context, MainActivity.NOTIFICATION_CHANNEL_ID)

            notificationBuilder!!
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(false)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setVibrate(longArrayOf(1000, 1000))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .addAction(R.drawable.ic_launcher_background, "Direct", stopServiceNotification(context))
                .addAction(R.drawable.ic_launcher_background, "From App", stopServiceNotificationFromActivity(context))
                .addAction(R.drawable.ic_launcher_background, "Foreground", stopServiceForegroundNotification(context))
                .priority = Notification.PRIORITY_MAX

        } else {
            notificationBuilder!!
                .setContentTitle(title)
                .setContentText(message)
        }

        val notification: Notification = notificationBuilder!!.build()

        //context.startForeground(NotificationId, notification)
        notificationManager.notify(NotificationId, notification)
    }

    private fun stopServiceForegroundNotification(context: Context) : PendingIntent {
        // This only stop foreground service in notification

        val intentStopSelf = Intent(context, MyService::class.java)
        intentStopSelf.action = MainActivity.TAG_ACTION_STOP_LISTEN
        return PendingIntent.getForegroundService(context, 0, intentStopSelf, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun stopServiceNotification(context: Context) : PendingIntent {
        // This will stop this whole service

        val intentStopSelf = Intent(context, StopServiceReceiver::class.java)
        intentStopSelf.action = MainActivity.TAG_ACTION_STOP_LISTEN
        return PendingIntent.getBroadcast(context, 0, intentStopSelf, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun stopServiceNotificationFromActivity(context: Context) : PendingIntent {
        val intentStopActivity = Intent(context, MainActivity::class.java)
        intentStopActivity.action = MainActivity.TAG_ACTION_STOP_LISTEN
        return PendingIntent.getActivity(context, 0, intentStopActivity, PendingIntent.FLAG_UPDATE_CURRENT)
    }
}