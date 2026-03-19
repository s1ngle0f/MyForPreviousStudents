package com.example.myforpreviousstudents

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.delay

class SyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            // имитация API вызова
            delay(2000)

            val userId = inputData.getInt("user_id", -1)

            Log.d("WorkManager", "Синхронизация завершена. User id: ${userId}")

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}