package defpackage.deskclock

import android.app.job.JobScheduler
import android.content.Context

val Context.jobScheduler: JobScheduler
    get() = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler