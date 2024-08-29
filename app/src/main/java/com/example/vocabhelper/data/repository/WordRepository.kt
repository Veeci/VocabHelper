package com.example.vocabhelper.data.repository

import com.example.vocabhelper.data.models.Response
import com.example.vocabhelper.data.models.WordData

interface WordRepository {

    suspend fun getWordFromAPI(word: String): List<Response>

    suspend fun saveWord(
        word: String,
        meaning: String?,
        category: String?,
        audioUrl: String?,
        synonym: String?,
        antonym: String?,
        collocation: String?,
        example: String?
    )

    fun updateWord(word: String, updatedData: Map<String, Any>)

    fun deleteWord(word: String)

    suspend fun getWords(): List<WordData>

    suspend fun getWordDetail(word: String): List<WordData>

    suspend fun getWordsByCategory(category: String): List<WordData>
}