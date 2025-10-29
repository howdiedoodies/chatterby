package com.howdiedoodies.chatterby.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.howdiedoodies.chatterby.data.AppDatabase
import com.howdiedoodies.chatterby.scraper.ChaturbateScraper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class OnlineStatusWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    private val scraper = ChaturbateScraper()

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val dao = AppDatabase.getDatabase(applicationContext).favoriteDao()
        val all = dao.getAll().first()

        all.forEach { fave ->
            val cams = scraper.search(query = fave.username)
            val cam = cams.firstOrNull()
            if (cam != null) {
                dao.updateStatus(fave.username, true, System.currentTimeMillis())
                dao.updateDetails(fave.username, null, cam.age, cam.location, System.currentTimeMillis())
            } else {
                dao.updateStatus(fave.username, false, fave.lastOnline)
            }
        }
        Result.success()
    }
}
