package com.howdiedoodies.chatterby.data

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object NetworkModule {

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://chaturbate.com/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val api: ChaturbateApi = retrofit.create(ChaturbateApi::class.java)
}
