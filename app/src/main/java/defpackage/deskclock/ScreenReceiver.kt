package defpackage.deskclock

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

@SuppressLint("UnsafeProtectedBroadcastReceiver")
class ScreenReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        AlarmService.launch(context)
    }
}