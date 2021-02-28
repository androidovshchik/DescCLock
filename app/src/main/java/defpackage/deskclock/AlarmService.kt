package defpackage.deskclock

import android.content.Context
import android.content.Intent
import androidx.annotation.WorkerThread
import androidx.core.app.JobIntentService
import com.android.deskclock.provider.Alarm
import org.jetbrains.anko.intentFor
import org.threeten.bp.LocalTime
import timber.log.Timber

class AlarmService : JobIntentService() {

    @WorkerThread
    override fun onHandleWork(intent: Intent) {
        Timber.e("onHandleWork")
        try {
            val time = LocalTime.now().plusHours(8)
            Timber.e("hour ${time.hour} minute ${time.minute}")
            Timber.e(Alarm.getAlarms(contentResolver, null).toString())
            Alarm().apply {
                deleteAfterUse = true
            }
            /*Alarm.getAlarms(contentResolver, null)
            val alarm = Alarm()
            alarm.hour = 0
            alarm.minutes = 0
            alarm.enabled = true
            Alarm.addAlarm(contentResolver, alarm)
            // Be ready to scroll to this alarm on UI later.
            // Create and add instance to db
            if (alarm.enabled) {
                setupAlarmInstance(newAlarm)
            }*/
        } catch (e: Throwable) {
            Timber.e(e)
        } finally {

        }
    }

    /*override fun onStartJob(params: JobParameters): Boolean {
        Timber.e("onStartJob")
        powerManager.isInteractive
        return false
    }

    override fun onStopJob(params: JobParameters): Boolean {
        Timber.e("onStopJob")
        return false
    }*/

    companion object {

        fun launch(context: Context) {
            with(context) {
                /*val job = JobInfo.Builder(100, ComponentName(applicationContext, AlarmService::class.java)).run {
                    setPeriodic(15 * 60_000L)
                    //setRequiresDeviceIdle(true)
                    setPersisted(true)
                    //setBackoffCriteria()
                    build()
                }
                jobScheduler.schedule(job)*/
                enqueueWork(
                    applicationContext,
                    AlarmService::class.java,
                    100,
                    intentFor<AlarmService>()
                )
            }
        }
    }
}