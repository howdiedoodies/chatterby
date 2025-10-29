package com.howdiedoodies.chatterby.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.howdiedoodies.chatterby.data.AppDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.howdiedoodies.chatterby.data.Favorite
import com.howdiedoodies.chatterby.worker.OnlineStatusWorker
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    private val favoriteDao = AppDatabase.getDatabase(application).favoriteDao()
    val favorites: StateFlow<List<Favorite>> = favoriteDao.getAll().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    private val workManager = WorkManager.getInstance(application)

    fun refreshNow() {
        val request = OneTimeWorkRequestBuilder<OnlineStatusWorker>().build()
        workManager.enqueue(request)
    }

    fun addFavorite(username: String) {
        viewModelScope.launch {
            favoriteDao.insert(Favorite(username = username))
        }
    }

    fun removeFavorite(favorite: Favorite) {
        viewModelScope.launch {
            favoriteDao.delete(favorite)
        }
    }
}