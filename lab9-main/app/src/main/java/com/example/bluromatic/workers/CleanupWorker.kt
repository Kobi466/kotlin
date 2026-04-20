package com.example.bluromatic.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.bluromatic.DELAY_TIME_MILLIS
import com.example.bluromatic.OUTPUT_PATH
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.File

private const val TAG = "CleanupWorker"

class CleanupWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        makeStatusNotification("Cleaning up old temporary files…", applicationContext)
        delay(DELAY_TIME_MILLIS)

        return@withContext try {
            val outputDirectory = File(applicationContext.filesDir, OUTPUT_PATH)
            if (outputDirectory.exists()) {
                outputDirectory.listFiles()
                    ?.filter { it.name.endsWith(".png") }
                    ?.forEach { file ->
                        val deleted = file.delete()
                        Log.i(TAG, "Deleted ${file.name} → $deleted")
                    }
            }
            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "Error cleaning up", e)
            Result.failure()
        }
    }
}