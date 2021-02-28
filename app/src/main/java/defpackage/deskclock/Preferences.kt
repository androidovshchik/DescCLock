package defpackage.deskclock

import android.content.Context
import com.chibatching.kotpref.KotprefModel

class Preferences(context: Context) : KotprefModel(context) {

    var alarmTime by longPref(8L, "alarm_time")

    var alarmId by longPref(0L, "alarm_id")
}