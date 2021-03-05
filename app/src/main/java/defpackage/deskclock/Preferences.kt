package defpackage.deskclock

import android.content.Context
import com.chibatching.kotpref.KotprefModel

@Suppress("PropertyName")
class Preferences(context: Context) : KotprefModel(context) {

    var _alarmId by longPref(0L, "alarm_id")

    var _reserveId by longPref(0L, "reserve_id")

    var autoEnabled by booleanPref(true, "auto_enabled")

    var alarmTime by longPref(480L, "alarm_time")

    var reserveEnabled by booleanPref(true, "reserve_enabled")

    var reserveTime by longPref(5L, "reserve_time")

    var runService by booleanPref(false, "run_service")
}