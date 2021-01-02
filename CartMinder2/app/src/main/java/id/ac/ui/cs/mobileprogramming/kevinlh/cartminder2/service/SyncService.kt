package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.service

import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import android.util.Log
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.database.repository.ApiRepository
import kotlinx.coroutines.runBlocking

class SyncService : JobService() {
    private lateinit var apiRepo: ApiRepository
    private var jobCancelled: Boolean = false

    override fun onStartJob(params: JobParameters?): Boolean {
        apiRepo = ApiRepository(application)
        runSync(params)
        return true
    }

    private fun runSync(params: JobParameters?) {
        Thread {
            if (jobCancelled) return@Thread
            Log.d(TAG, "sync job started")
            runBlocking {
                val receivedCarts = apiRepo.getCarts()
                val cartApiModelPosts = apiRepo.convertCartApiModelToPosts(receivedCarts)
                apiRepo.saveCartApisToLocal(cartApiModelPosts)
            }
            jobFinished(params, false)
        }.start()
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        Log.d(TAG, "Job sync is cancelled")
        jobCancelled = true
        return false
    }

    companion object {
        private const val TAG = "SyncService"
        private const val JOB_ID = 17

        fun scheduleJob(ctx: Context) {
            val componentName: ComponentName = ComponentName(ctx, SyncService::class.java)
            val jobInfo: JobInfo = JobInfo.Builder(JOB_ID, componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setPersisted(true)
                .setPeriodic(12 * 60 * 60 * 1000)   // run every 12 hours
                .build()
            val scheduler: JobScheduler =
                ctx.getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
            val resultCode: Int = scheduler.schedule(jobInfo)
            if (resultCode == JobScheduler.RESULT_SUCCESS) Log.d(TAG, "Job Scheduled")
            else Log.d(TAG, "Job Failed to Scheduled")
        }

        fun cancelJob(ctx: Context) {
            val scheduler: JobScheduler =
                ctx.getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
            scheduler.cancel(JOB_ID)
        }
    }
}