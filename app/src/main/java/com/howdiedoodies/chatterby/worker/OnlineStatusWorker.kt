package com.howdiedoodies.chatterby.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.howdiedoodies.chatterby.data.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

class OnlineStatusWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val dao = AppDatabase.getDatabase(applicationContext).favoriteDao()
            val favorites = dao.getAll()
            for (favorite in favorites) {
                val url = "https://chaturbate.com/${favorite.username}"
                val doc = Jsoup.connect(url).get()
                val isOnline = doc.select(".status.online").isNotEmpty()
                val subject = doc.select(".subject").text()
                val age = doc.select(".age").text().toIntOrNull()
                val gender = doc.select(".gender").text()
                val location = doc.select(".location").text()
                val thumbnail = doc.select(".thumbnail img").attr("src")

                val updatedFavorite = favorite.copy(
                    isOnline = isOnline,
                    subject = subject,
                    age = age,
                    gender = gender,
                    location = location,
                    lastOnline = if (isOnline) System.currentTimeMillis() else favorite.lastOnline,
                    thumbnailPath = thumbnail
                )
                dao.update(updatedFavorite)
            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}
