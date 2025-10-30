package com.howdiedoodies.chatterby.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.howdiedoodies.chatterby.data.AppDatabase
import com.howdiedoodies.chatterby.data.Favorite
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    private val favoriteDao = AppDatabase.getDatabase(application).favoriteDao()
    val favorites: StateFlow<List<Favorite>> = favoriteDao.getAll().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

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

    fun refreshFavorites() {
        val workManager = androidx.work.WorkManager.getInstance(getApplication())
        val request = androidx.work.OneTimeWorkRequestBuilder<com.howdiedoodies.chatterby.worker.OnlineStatusWorker>().build()
        workManager.enqueue(request)
    }
}