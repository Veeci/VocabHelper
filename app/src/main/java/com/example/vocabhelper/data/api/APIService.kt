package com.example.vocabhelper.data.api

import com.example.vocabhelper.data.models.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

private val retrofit = Retrofit.Builder()
    .baseUrl("https://api.dictionaryapi.dev/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

interface APIService {
    @GET("api/v2/entries/en/{word}")
    suspend fun getWord(@Path("word") word: String): List<Response>

}

val apiService: APIService = retrofit.create(APIService::class.java)