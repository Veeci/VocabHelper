package com.veeci.vocabhelper.data.implementation

import com.veeci.vocabhelper.data.api.APIService
import com.veeci.vocabhelper.data.models.Response
import com.veeci.vocabhelper.data.models.WordData
import com.veeci.vocabhelper.data.repository.WordRepository

class WordRepoImplementation(private val apiService: APIService) : WordRepository {
    override suspend fun getWordFromAPI(word: String): List<Response> {
        return apiService.getWord(word)
    }

    override suspend fun saveWord(
        word: String,
        meaning: String?,
        category: String?,
        audioUrl: String?,
        synonym: String?,
        antonym: String?,
        collocation: String?,
        example: String?
    ) {

    }

    override fun updateWord(word: String, updatedData: Map<String, Any>) {
        TODO("Not yet implemented")
    }

    override fun deleteWord(word: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getWords(): List<WordData> {
        TODO("Not yet implemented")
    }

    override suspend fun getWordDetail(word: String): List<WordData> {
        TODO("Not yet implemented")
    }

    override suspend fun getWordsByCategory(category: String): List<WordData> {
        TODO("Not yet implemented")
    }
}