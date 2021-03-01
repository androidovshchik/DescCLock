package defpackage.deskclock

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.android.deskclock.DeskClock
import com.android.deskclock.NotificationUtils
import com.android.deskclock.R
import org.jetbrains.anko.intentFor

class ForegroundService : Service() {

    private val receiver = ScreenReceiver()

    override fun onBind(intent: Intent): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        startForeground(
            Int.MAX_VALUE,
            NotificationCompat.Builder(
                applicationContext,
                NotificationUtils.ALARM_SNOOZE_NOTIFICATION_CHANNEL_ID
            ).setSmallIcon(R.drawable.ic_alarm)
                .setContentTitle("Фоновой сервис")
                .setContentIntent(pendingActivityFor<DeskClock>())
                .setOngoing(true)
                .setSound(null)
                .build()
        )
        registerReceiver(receiver, IntentFilter().apply {
            addAction(Intent.ACTION_BATTERY_CHANGED)
            addAction(Intent.ACTION_BATTERY_LOW)
            addAction(Intent.ACTION_BATTERY_OKAY)
        })
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()
    }

    companion object {

        fun start(context: Context?) {
            context?.run {
                ContextCompat.startForegroundService(
                    applicationContext,
                    intentFor<ForegroundService>()
                )
            }
        }

        fun stop(context: Context?) {
            context?.run {
                stopService(intentFor<ForegroundService>())
            }
        }
    }
}