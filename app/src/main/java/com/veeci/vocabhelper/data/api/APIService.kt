package com.veeci.vocabhelper.data.api

import androidx.annotation.Keep
import com.veeci.vocabhelper.data.models.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

@Keep
interface APIService {
    @GET("entries/en/{word}")
    suspend fun getWord(@Path("word") word: String): List<Response>

    companion object {
        private const val BASE_URL = "https://api.dictionaryapi.dev/api/v2/"

        fun create(): APIService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(APIService::class.java)
        }
    }
}
