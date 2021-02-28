package defpackage.deskclock

import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import com.android.deskclock.R
import com.android.deskclock.alarms.AlarmStateManager
import com.android.deskclock.data.Weekdays
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
        val time = LocalTime.now().plusMinutes(preferences.alarmTime)
        val alarm = Alarm(time.hour, time.minute).apply {
            label = "Автоматический будильник"
            daysOfWeek = Weekdays.ALL
            enabled = true
            deleteAfterUse = true
        }
        val id = preferences.alarmId
        future = doAsync {
            // deleting previous alarm
            AlarmStateManager.deleteAllInstances(applicationContext, id)
            Alarm.deleteAlarm(contentResolver, id)
            // creating next alarm
            Events.sendAlarmEvent(R.string.action_create, R.string.label_deskclock)
            val newAlarm = Alarm.addAlarm(contentResolver, alarm)
            preferences.alarmId = alarm.id
            var newInstance = newAlarm.createInstanceAfter(Calendar.getInstance())
            newInstance = AlarmInstance.addInstance(contentResolver, newInstance)
            AlarmStateManager.registerInstance(applicationContext, newInstance, true)
            jobFinished(params, false)
        }
        return true
    }

    override fun onStopJob(params: JobParameters): Boolean {
        future?.cancel(true)
        return false
    }

    companion object {

        fun launch(context: Context) {
            with(context) {
                val job = JobInfo.Builder(100, component<AlarmService>()).run {
                    build()
                }
                jobScheduler.schedule(job)
            }
        }
    }
}