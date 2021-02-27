package defpackage.deskclock

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.core.app.JobIntentService
import org.jetbrains.anko.intentFor

class AlarmService : JobIntentService() {

    @WorkerThread
    override fun onHandleWork(intent: Intent) {
        try {

        } catch (ignored: IllegalArgumentException) {
        } catch (e: Throwable) {
            Log.e("TAG", e.message, e)
        } finally {

        }
    }

    companion object {

        fun launch(context: Context, vararg params: Pair<String, Any?>) {
            with(context) {
                enqueueWork(
                    applicationContext,
                    AlarmService::class.java,
                    100,
                    intentFor<AlarmService>(*params)
                )
            }
        }
    }
}