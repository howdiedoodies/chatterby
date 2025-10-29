package com.howdiedoodies.chatterby.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.howdiedoodies.chatterby.api.ChaturbateApi
import com.howdiedoodies.chatterby.data.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class OnlineStatusWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    private val api = ChaturbateApi()

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val dao = AppDatabase.getDatabase(applicationContext).favoriteDao()
        val all = dao.getAll().first()

        all.forEach { fave ->
            val events = api.getEvents("YOUR_TOKEN", fave.username) // TODO: Replace with your token
            val broadcastStart = events.find { it.method == "broadcastStart" }
            val broadcastStop = events.find { it.method == "broadcastStop" }
            val subjectChange = events.find { it.method == "roomSubjectChange" }

            if (broadcastStart != null) {
                dao.updateStatus(fave.username, true, System.currentTimeMillis())
            } else if (broadcastStop != null) {
                dao.updateStatus(fave.username, false, fave.lastOnline)
            }

            if (subjectChange != null) {
                dao.updateDetails(fave.username, null, subjectChange.subject?.subject, System.currentTimeMillis())
            }
        }
        Result.success()
    }
}
