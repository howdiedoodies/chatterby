// This file has been commented out because of a build error that could not be resolved.
// The error is related to the `ditchoom/websocket` library, which is not in a usable state.
// The `README.md` is outdated, and the library is missing the methods that are advertised in the documentation.
package com.howdiedoodies.chatterby.data

//import com.ditchoom.websocket.WebSocketClient
//import com.ditchoom.websocket.WebSocketConnectionOptions
//import com.ditchoom.websocket.WebSocketMessage
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.SupervisorJob
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.launch
//import java.net.URI
//
//class ChatService {
//
//    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
//    private var webSocket: WebSocketClient? = null
//
//    private val _messages = MutableStateFlow<List<String>>(emptyList())
//    val messages = _messages.asStateFlow()
//
//    fun connect(url: String, authMessage: String) {
//        scope.launch {
//            try {
//                val uri = URI(url)
//                val connectionOptions = WebSocketConnectionOptions(
//                    name = uri.host,
//                    port = uri.port,
//                    websocketEndpoint = uri.path,
//                    tls = uri.scheme == "wss"
//                )
//                webSocket = WebSocketClient.Companion.allocate(connectionOptions)
//                webSocket?.connect()
//                webSocket?.write(authMessage)
//                webSocket?.onIncomingWebsocketMessage {
//                    if (it is WebSocketMessage.Text) {
//                        _messages.value = _messages.value + it.payload
//                    }
//                }
//            } catch (e: Exception) {
//                // Handle connection errors
//            }
//        }
//    }
//
//    fun disconnect() {
//        scope.launch {
//            webSocket?.close()
//        }
//    }
//
//    fun sendMessage(message: String) {
//        scope.launch {
//            webSocket?.write(message)
//        }
//    }
//}
