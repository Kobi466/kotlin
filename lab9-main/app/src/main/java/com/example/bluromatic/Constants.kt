package com.example.bluromatic

const val TAG_OUTPUT = "OUTPUT"

const val VERBOSE_NOTIFICATION_CHANNEL_NAME = "Verbose WorkManager Notifications"
const val VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION = "Shows notifications whenever work starts"
const val NOTIFICATION_TITLE = "WorkRequest Starting"
const val CHANNEL_ID = "VERBOSE_NOTIFICATION"
const val NOTIFICATION_ID = 1

const val OUTPUT_PATH = "blur_filter_outputs"
const val DELAY_TIME_MILLIS: Long = 3_000L

object WorkerKeys {
    const val IMAGE_URI = "IMAGE_URI"
    const val BLUR_AMOUNT = "BLUR_AMOUNT"
}

const val IMAGE_MANIPULATION_WORK_NAME = "image_manipulation_work"