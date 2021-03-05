package defpackage.deskclock

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

@SuppressLint("UnsafeProtectedBroadcastReceiver")
class ScreenReceiver : BroadcastReceiver() {

    var isManualRegistered = false

    override fun onReceive(context: Context, intent: Intent) {
        val preferences = Preferences(context)
        if (preferences.autoEnabled) {
            if (!preferences.runService || isManualRegistered) {
                AlarmService.launch(context)
            }
        }
    }
}