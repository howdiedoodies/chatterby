package com.howdiedoodies.chatterby.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ChatMessage(val user: String, val text: String)

data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val currentMessage: String = ""
)

class ChatViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    init {
        // Add some mock messages for display
        _uiState.value = ChatUiState(
            messages = listOf(
                ChatMessage("User1", "Hello!"),
                ChatMessage("User2", "Hi there! How are you?"),
                ChatMessage("User1", "I'm good, thanks! This is a mock chat.")
            )
        )
    }

    fun onMessageChanged(message: String) {
        _uiState.update { it.copy(currentMessage = message) }
    }

    fun sendMessage() {
        if (_uiState.value.currentMessage.isBlank()) return

        val newMessage = ChatMessage("Me", _uiState.value.currentMessage)
        _uiState.update {
            it.copy(
                messages = it.messages + newMessage,
                currentMessage = ""
            )
        }
    }
}
