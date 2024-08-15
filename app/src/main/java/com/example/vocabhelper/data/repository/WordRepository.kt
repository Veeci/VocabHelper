package com.example.vocabhelper.data.repository

import com.example.vocabhelper.data.api.APIService
import com.example.vocabhelper.data.models.Response

class WordRepository(private val apiService: APIService)
{
    suspend fun getWord(word: String): List<Response> {
        return apiService.getWord(word)
    }

}