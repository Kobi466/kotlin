package com.example.bluromatic.workers

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.bluromatic.DELAY_TIME_MILLIS
import com.example.bluromatic.WorkerKeys
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

private const val TAG = "BlurWorker"

class BlurWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {

    override suspend fun doWork(): Result {
        makeStatusNotification("🎨 Blurring image…", applicationContext)
        delay(DELAY_TIME_MILLIS)

        return withContext(Dispatchers.IO) {
            val resourceUri = inputData.getString(WorkerKeys.IMAGE_URI)

            if (resourceUri.isNullOrEmpty()) {
                Log.e(TAG, "No IMAGE_URI in inputData")
                return@withContext Result.failure()
            }

            return@withContext try {
                val resolver = applicationContext.contentResolver
                val sourceBitmap = BitmapFactory.decodeStream(
                    resolver.openInputStream(Uri.parse(resourceUri))
                ) ?: throw IllegalStateException("Cannot decode bitmap")

                val blurredBitmap = blurBitmap(sourceBitmap)
                val outputUri = writeBitmapToFile(applicationContext, blurredBitmap)

                Log.i(TAG, "Blurred output → $outputUri")
                Result.success(workDataOf(WorkerKeys.IMAGE_URI to outputUri.toString()))
            } catch (throwable: Throwable) {
                Log.e(TAG, "Error applying blur", throwable)
                Result.failure()
            }
        }
    }
}