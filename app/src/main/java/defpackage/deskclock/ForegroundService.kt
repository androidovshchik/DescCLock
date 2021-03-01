package defpackage.deskclock

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.android.deskclock.DeskClock
import com.android.deskclock.R
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.notificationManager

class ForegroundService : Service() {

    private val receiver = ScreenReceiver()

    override fun onBind(intent: Intent): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        if (isOreoPlus()) {
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    "foreground",
                    "Foreground",
                    NotificationManager.IMPORTANCE_LOW
                )
            )
        }
        startForeground(
            Int.MAX_VALUE, NotificationCompat.Builder(applicationContext, "foreground")
                .setSmallIcon(R.drawable.ic_alarm_time)
                .setContentTitle("Фоновой сервис")
                .setContentIntent(pendingActivityFor<DeskClock>())
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setOngoing(true)
                .setSound(null)
                .build()
        )
        registerReceiver(receiver, IntentFilter().apply {
            addAction(Intent.ACTION_USER_PRESENT)
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