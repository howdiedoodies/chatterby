package com.howdiedoodies.chatterby.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.howdiedoodies.chatterby.data.AppDatabase
import com.howdiedoodies.chatterby.data.Favorite
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup

class SyncFavoritesWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    private val client = OkHttpClient()

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url("https://chaturbate.com/followed-cams/")
                .build()
            val response = client.newCall(request).execute()
            if (!response.isSuccessful) return@withContext Result.retry()

            val html = response.body?.string() ?: return@withContext Result.failure()
            val doc = Jsoup.parse(html)
            val usernames = doc.select("a.room-card__username")
                .mapNotNull { it.text().trim().takeIf { it.isNotBlank() } }
                .distinct()

            val dao = AppDatabase.getDatabase(applicationContext).favoriteDao()
            val favorites = usernames.map { Favorite(username = it) }
            dao.insertAll(favorites)

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
