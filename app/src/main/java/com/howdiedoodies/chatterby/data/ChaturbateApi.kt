package com.howdiedoodies.chatterby.data

import retrofit2.http.GET
import retrofit2.http.Query

interface ChaturbateApi {

    // NOTE: This is an unofficial API endpoint discovered through reverse-engineering.
    // It may be unstable or change without notice.
    @GET("api/chaturbate/search/results/")
    suspend fun search(
        @Query("query") query: String,
        @Query("usernames") usernames: String? = null
    ): SearchResult
}
