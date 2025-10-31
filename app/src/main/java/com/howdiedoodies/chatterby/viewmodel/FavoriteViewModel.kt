package com.howdiedoodies.chatterby.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.howdiedoodies.chatterby.data.AppDatabase
import com.howdiedoodies.chatterby.data.Favorite
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    private val favoriteDao = AppDatabase.getDatabase(application).favoriteDao()
    val favorites: StateFlow<List<Favorite>> = favoriteDao.getAll().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    private val _isRefreshing = kotlinx.coroutines.flow.MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

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
        viewModelScope.launch {
            _isRefreshing.value = true
            // TODO: This is inefficient. We should explore a batch API endpoint if one exists,
            // or at least introduce some concurrency here.
            favorites.value.forEach { favorite ->
                try {
                    val searchResult = com.howdiedoodies.chatterby.data.NetworkModule.api.search(favorite.username)
                    val cam = searchResult.results.firstOrNull { it.username.equals(favorite.username, ignoreCase = true) }
                    val isOnline = cam?.roomStatus == "public"
                    favoriteDao.updateStatus(favorite.username, isOnline, System.currentTimeMillis())
                } catch (e: Exception) {
                    // Handle network errors
                }
            }
            _isRefreshing.value = false
        }
    }
}