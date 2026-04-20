package com.example.bluromatic.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.bluromatic.CHANNEL_ID
import com.example.bluromatic.NOTIFICATION_ID
import com.example.bluromatic.NOTIFICATION_TITLE
import com.example.bluromatic.OUTPUT_PATH
import com.example.bluromatic.VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION
import com.example.bluromatic.VERBOSE_NOTIFICATION_CHANNEL_NAME
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

fun makeStatusNotification(message: String, context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            VERBOSE_NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply { description = VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION }

        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.createNotificationChannel(channel)
    }

    val notification = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(android.R.drawable.ic_menu_gallery)
        .setContentTitle(NOTIFICATION_TITLE)
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setVibrate(LongArray(0))
        .build()

    val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    nm.notify(NOTIFICATION_ID, notification)
}

fun blurBitmap(bitmap: Bitmap): Bitmap {
    val scaleFactor = 8
    val smallBitmap = Bitmap.createScaledBitmap(
        bitmap,
        (bitmap.width / scaleFactor).coerceAtLeast(1),
        (bitmap.height / scaleFactor).coerceAtLeast(1),
        false
    )
    return Bitmap.createScaledBitmap(smallBitmap, bitmap.width, bitmap.height, true)
}

fun writeBitmapToFile(applicationContext: Context, bitmap: Bitmap): Uri {
    val outputDir = File(applicationContext.filesDir, OUTPUT_PATH).also { it.mkdirs() }
    val outputFile = File(outputDir, "blur-output-${UUID.randomUUID()}.png")
    FileOutputStream(outputFile).use { out ->
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
    }
    return Uri.fromFile(outputFile)
}