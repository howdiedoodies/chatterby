package com.howdiedoodies.chatterby.api

import com.google.gson.Gson
import com.howdiedoodies.chatterby.api.model.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

class ChaturbateApi {
    private val client = OkHttpClient()
    private val gson = Gson()

    suspend fun getEvents(token: String, room: String): List<Event> = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url("https://events.chaturbate.com/events/?token=$token&room=$room")
            .build()
        val response = client.newCall(request).execute()
        if (response.isSuccessful) {
            val json = response.body?.string()
            gson.fromJson(json, Array<Event>::class.java).toList()
        } else {
            emptyList()
        }
    }
}
