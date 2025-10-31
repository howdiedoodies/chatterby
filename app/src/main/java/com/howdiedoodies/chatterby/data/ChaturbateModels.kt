package com.howdiedoodies.chatterby.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchResult(
    @Json(name = "results") val results: List<Cam>,
    @Json(name = "next_url") val nextUrl: String
)

@JsonClass(generateAdapter = true)
data class Cam(
    @Json(name = "username") val username: String,
    @Json(name = "gender") val gender: String,
    @Json(name = "image_url") val imageUrl: String,
    @Json(name = "is_hd") val isHd: Boolean,
    @Json(name = "room_status") val roomStatus: String,
    @Json(name = "age") val age: Int,
    @Json(name = "num_users") val numUsers: Int,
    @Json(name = "room_pass") val roomPass: String
)
