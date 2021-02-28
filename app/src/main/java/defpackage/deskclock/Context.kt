package defpackage.deskclock

import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context

inline fun <reified T> Context.component() = ComponentName(applicationContext, T::class.java)

val Context.jobScheduler: JobScheduler
    get() = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler