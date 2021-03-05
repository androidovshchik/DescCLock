package defpackage.deskclock

import android.content.Context
import com.chibatching.kotpref.KotprefModel

class Preferences(context: Context) : KotprefModel(context) {

    var autoEnabled by booleanPref(true, "auto_enabled")

    var alarmTime by longPref(480L, "alarm_time")

    var alarmId by longPref(0L, "alarm_id")

    var runService by booleanPref(false, "run_service")
}