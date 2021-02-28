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
import timber.log.Timber
import java.util.*

class AlarmService : JobService() {

    override fun onStartJob(params: JobParameters): Boolean {
        try {
            val preferences = Preferences(applicationContext)
            val time = LocalTime.now().plusHours(preferences.alarmTime)
            val alarm = Alarm(time.hour, time.minute).apply {
                label = "Автоматический будильник"
                daysOfWeek = Weekdays.ALL
                enabled = true
                deleteAfterUse = true
            }
            val id = preferences.alarmId
            doAsync {
                // deleting previous alarm
                AlarmStateManager.deleteAllInstances(applicationContext, id)
                Alarm.deleteAlarm(contentResolver, id)
                // creating next alarm
                Events.sendAlarmEvent(R.string.action_create, R.string.label_deskclock)
                val newAlarm = Alarm.addAlarm(contentResolver, alarm)
                var newInstance = newAlarm.createInstanceAfter(Calendar.getInstance())
                newInstance = AlarmInstance.addInstance(contentResolver, newInstance)
                AlarmStateManager.registerInstance(applicationContext, newInstance, true)
            }
        } catch (e: Throwable) {
            Timber.e(e)
        } finally {

        }
        return false
    }

    override fun onStopJob(params: JobParameters): Boolean {
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