package defpackage.deskclock

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import timber.log.Timber

class ScreenReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Timber.e("AlarmReceiver ${intent.action}")
        AlarmService.launch(context)
        //AlarmService.launch(context)
    }

    companion object {

        fun setup(context: Context) {
            with(context) {
                /*val now = Instant.now().toEpochMilli()
                val refreshTime = Firebase.remoteConfig.getLong(CFG_DATA_REFRESH_HOURS) * hour
                val preferences = Preferences(applicationContext)
                val lastDownload = preferences.lastDownload
                val spentTime = now - lastDownload
                val interval = if (spentTime >= refreshTime) {
                    DownloadWorker.launch(context)
                    preferences.lastDownload = now
                    refreshTime
                } else {
                    if (BuildConfig.DEBUG) {
                        DownloadWorker.launch(context)
                    }
                    refreshTime - spentTime
                }
                Timber.d("Next alarm ${DateUtils.formatElapsedTime(interval / 1000)}")
                AlarmManagerCompat.setExactAndAllowWhileIdle(
                    alarmManager,
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + interval,
                    pendingReceiverFor(intentFor<DownloadReceiver>().also {
                        it.data = Uri.parse("$packageName://1")
                    }, 1)
                )*/
            }
        }
    }
}