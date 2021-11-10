package com.example.simplealarmmanager

import android.app.ActivityManager
import android.content.*
import android.os.*
import android.widget.Toast
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.simplealarmmanager.AlarmManager.AlarmReceiver
import com.example.simplealarmmanager.Service.MyService
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "Sample_Channel"
        const val ACTION_TIMER_DATA = "action_timer_data"

        const val TAG_TIMER_DATA = "timer_data"
        const val TAG_CAN_SHOW_TIMER_DATA_FOREEGROUND = "can_show_timer_data_notif"
        const val TAG_ACTION_STOP_LISTEN = "action_stop_listen"
        const val TAG_ACTION_AUTO_STOP_SERVICE = "auto_stop_service"
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var toast: Toast? = null

    private var timerText = ""
    private var alarm = AlarmReceiver()
    private var mService: MyService? = null
    private var mBound: Boolean = false

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as MyService.LocalBinder
            mService = binder.getService()
            mBound = true

            registerLocalBroadcastReceiver()

            showToast("Service Started")

            checkButtonServiceEnable()
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mService = null
            mBound = false

            unRegisterLocalBroadcastReceiver()

            showToast("Service Stopped")

            checkButtonServiceEnable()
        }
    }

    private val mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            timerText = intent.getStringExtra(TAG_TIMER_DATA) ?: "00 : 00 : 00"

            tvTime.text = timerText
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkButtonServiceEnable()
        setupClick()
    }

    override fun onResume() {
        super.onResume()

        if(mService == null) return
        mService!!.stopForegroundService(removeNotification = true)
    }

    override fun onStart() {
        super.onStart()

        val needStopServiceAutomatic: Boolean = when(intent.action) {
            TAG_ACTION_STOP_LISTEN -> true
            else -> !isMyServiceRunning(MyService::class.java)
        }

        startService(needStopServiceAutomatic)
    }

    override fun onStop() {
        super.onStop()

        if(!isMyServiceRunning(MyService::class.java)) return
        startForegroundService()
    }

    private fun setupClick() {
        btnStartAlarm.setOnClickListener {
            alarm.setAlarm(this)
        }

        btnEndAlarm.setOnClickListener {
            alarm.cancelAlarm(this)
        }

        btnStartService.setOnClickListener {
            if (mBound) return@setOnClickListener
            startService()
        }

        btnStopService.setOnClickListener {
            if (!mBound) return@setOnClickListener
            stopService()
        }
    }

    private fun startService(needStopServiceAutomatic: Boolean = false) {
        val intent = Intent(this, MyService::class.java)
        intent.putExtra(TAG_ACTION_AUTO_STOP_SERVICE, needStopServiceAutomatic)

        startService(intent)
        bindService(intent, connection, Context.BIND_NOT_FOREGROUND)

        // this will run automatically and cannot be destroyed
        // bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    private fun stopService() {
        Intent(this, MyService::class.java).also { intent ->
            stopService(intent)
        }
    }

    private fun startForegroundService() {
        Intent(this, MyService::class.java).also { intent ->
            intent.putExtra(TAG_CAN_SHOW_TIMER_DATA_FOREEGROUND, true)

            ContextCompat.startForegroundService(this, intent)
        }
    }

    private fun checkButtonServiceEnable() {
        if(mBound) {
            btnStartService.alpha = 0.3f
            btnStopService.alpha = 1f
        } else {
            btnStartService.alpha = 1f
            btnStopService.alpha = 0.3f
        }
    }

    private fun registerLocalBroadcastReceiver() {
        LocalBroadcastManager
            .getInstance(this@MainActivity)
            .registerReceiver(mMessageReceiver, IntentFilter(ACTION_TIMER_DATA))
    }

    private fun unRegisterLocalBroadcastReceiver() {
        LocalBroadcastManager
            .getInstance(this@MainActivity)
            .unregisterReceiver(mMessageReceiver)
    }

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) return true
        }
        return false
    }

    fun showToast(text: String) {
        toast = Toast.makeText(this, text, Toast.LENGTH_LONG)
        toast?.show()
    }
}