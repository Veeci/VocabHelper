package com.example.vocabhelper.data.repository

import com.example.vocabhelper.data.api.APIService
import com.example.vocabhelper.data.database.WordDAO
import com.example.vocabhelper.data.database.WordEntity
import com.example.vocabhelper.data.models.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WordRepository(private val apiService: APIService, private val wordDAO: WordDAO) {

    suspend fun getWordFromAPI(word: String): List<Response> {
        return apiService.getWord(word)
    }

    suspend fun getWordFromDb(word: String): List<Response>? {
        return withContext(Dispatchers.IO) {
            wordDAO.getWord(word)?.let { wordEntity ->
                listOf(
                    Response(
                        word = wordEntity.word,
                        phonetic = wordEntity.phonetic,
                        phonetics = wordEntity.phonetics,
                        meanings = wordEntity.meanings,
                        license = wordEntity.license,
                        sourceUrls = wordEntity.sourceUrls
                    )
                )
            }
        }
    }

    suspend fun insertWord(wordEntity: WordEntity) {
        withContext(Dispatchers.IO) {
            wordDAO.insertWord(wordEntity)
        }
    }

    suspend fun deleteWord(word: String) {
        withContext(Dispatchers.IO) {
            wordDAO.deleteWord(word)
        }
    }
}
