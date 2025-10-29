package com.howdiedoodies.chatterby.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.howdiedoodies.chatterby.data.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.first
import okhttp3.OkHttpClient
import okhttp3.Request

class OnlineStatusWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    private val client = OkHttpClient()

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val dao = AppDatabase.getDatabase(applicationContext).favoriteDao()
        val all = dao.getAll().first()

        all.forEach { fave ->
            val isOnline = isUserOnline(fave.username)
            val timestamp = if (isOnline) System.currentTimeMillis() else fave.lastOnline
            dao.updateStatus(fave.username, isOnline, timestamp)
        }
        Result.success()
    }

    private suspend fun isUserOnline(username: String): Boolean = try {
        val req = Request.Builder()
            .url("https://chaturbate.com/api/chat/videoviewers/?room=$username")
            .build()
        val resp = client.newCall(req).execute()
        resp.isSuccessful && resp.body?.string()?.contains("\"is_hd\":true") == true
    } catch (e: Exception) {
        false
    }
}
