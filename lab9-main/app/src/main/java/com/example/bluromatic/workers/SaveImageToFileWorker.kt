package com.example.bluromatic.workers

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.bluromatic.DELAY_TIME_MILLIS
import com.example.bluromatic.WorkerKeys
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val TAG = "SaveImageToFileWorker"

class SaveImageToFileWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {

    private val dateFormatter = SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss z", Locale.getDefault())

    override suspend fun doWork(): Result {
        makeStatusNotification("Saving image to gallery…", applicationContext)
        delay(DELAY_TIME_MILLIS)

        return withContext(Dispatchers.IO) {
            return@withContext try {
                val resourceUri = inputData.getString(WorkerKeys.IMAGE_URI)
                    ?: return@withContext Result.failure()

                val resolver = applicationContext.contentResolver
                val bitmap = BitmapFactory.decodeStream(
                    resolver.openInputStream(Uri.parse(resourceUri))
                ) ?: return@withContext Result.failure()

                @Suppress("DEPRECATION")
                val imageUrl = MediaStore.Images.Media.insertImage(
                    resolver, bitmap,
                    "BlurOmatic Result",
                    "Blurred on ${dateFormatter.format(Date())}"
                )

                if (!imageUrl.isNullOrEmpty()) {
                    Result.success(workDataOf(WorkerKeys.IMAGE_URI to imageUrl))
                } else {
                    Log.e(TAG, "Failed to write to MediaStore")
                    Result.failure()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Unable to save image", e)
                Result.failure()
            }
        }
    }
}