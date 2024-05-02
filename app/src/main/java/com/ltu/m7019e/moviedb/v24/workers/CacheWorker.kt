package com.ltu.m7019e.moviedb.v24.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

private const val TAG = "CacheWorker"

class CacheWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
        return try{

            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}