package com.howdiedoodies.chatterby.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.howdiedoodies.chatterby.api.ChaturbateApi
import com.howdiedoodies.chatterby.api.model.Event
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RoomViewModel : ViewModel() {
    private val api = ChaturbateApi()
    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events
    private var pollingJob: Job? = null

    fun startPolling(room: String) {
        pollingJob?.cancel()
        pollingJob = viewModelScope.launch {
            while (true) {
                val newEvents = api.getEvents("YOUR_TOKEN", room) // TODO: Replace with your token
                _events.value = _events.value + newEvents
                delay(2000)
            }
        }
    }
}
