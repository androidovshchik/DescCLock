package defpackage.deskclock

import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.os.PersistableBundle
import com.android.deskclock.R
import com.android.deskclock.alarms.AlarmStateManager
import com.android.deskclock.events.Events
import com.android.deskclock.provider.Alarm
import com.android.deskclock.provider.AlarmInstance
import org.jetbrains.anko.doAsync
import org.threeten.bp.LocalTime
import java.util.*
import java.util.concurrent.Future

class AlarmService : JobService() {

    private var future: Future<Unit>? = null

    override fun onStartJob(params: JobParameters): Boolean {
        val preferences = Preferences(applicationContext)
        var time = LocalTime.now().plusMinutes(preferences.alarmTime)
        val alarm = Alarm(time.hour, time.minute).apply {
            label = "Основной будильник"
            enabled = true
            deleteAfterUse = true
        }
        time = time.plusMinutes(preferences.reserveTime)
        val alarmReserve = Alarm(time.hour, time.minute).apply {
            label = "Резервный будильник"
            enabled = true
            deleteAfterUse = true
        }
        val id = preferences._alarmId
        val reserveId = preferences._reserveId
        val reserveEnabled = preferences.reserveEnabled
        future = doAsync {
            // deleting previous alarm
            AlarmStateManager.deleteAllInstances(applicationContext, id)
            Alarm.deleteAlarm(contentResolver, id)
            if (reserveEnabled) {
                AlarmStateManager.deleteAllInstances(applicationContext, reserveId)
                Alarm.deleteAlarm(contentResolver, reserveId)
            }
            if (!params.extras.getBoolean("deleteOnly")) {
                // creating next alarm
                Events.sendAlarmEvent(R.string.action_create, R.string.label_deskclock)
                var newAlarm = Alarm.addAlarm(contentResolver, alarm)
                preferences._alarmId = alarm.id
                var newInstance = AlarmInstance.addInstance(
                    contentResolver,
                    newAlarm.createInstanceAfter(Calendar.getInstance())
                )
                AlarmStateManager.registerInstance(applicationContext, newInstance, true)
                if (reserveEnabled) {
                    Events.sendAlarmEvent(R.string.action_create, R.string.label_deskclock)
                    newAlarm = Alarm.addAlarm(contentResolver, alarmReserve)
                    preferences._reserveId = alarmReserve.id
                    newInstance = AlarmInstance.addInstance(
                        contentResolver,
                        newAlarm.createInstanceAfter(Calendar.getInstance())
                    )
                    AlarmStateManager.registerInstance(applicationContext, newInstance, true)
                }
            }
            jobFinished(params, false)
        }
        return true
    }

    override fun onStopJob(params: JobParameters): Boolean {
        future?.cancel(true)
        return false
    }

    companion object {

        fun launch(context: Context, deleteOnly: Boolean = false) {
            with(context) {
                val job = JobInfo.Builder(100, component<AlarmService>()).run {
                    setOverrideDeadline(0)
                    setExtras(PersistableBundle().apply {
                        putBoolean("deleteOnly", deleteOnly)
                    })
                    build()
                }
                jobScheduler.schedule(job)
            }
        }
    }
}