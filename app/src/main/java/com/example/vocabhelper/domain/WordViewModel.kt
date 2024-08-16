package com.example.vocabhelper.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.vocabhelper.data.database.WordEntity
import com.example.vocabhelper.data.repository.WordRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WordViewModel(private val repository: WordRepository) : ViewModel() {

    // Fetch word definition from API
    fun getWordDefinition(word: String) = liveData(Dispatchers.IO) {
        try {
            val response = repository.getWordFromAPI(word)
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    // Fetch word definition from the local database
    fun getWordFromDb(word: String) = liveData(Dispatchers.IO) {
        try {
            val wordEntity = repository.getWordFromDb(word)
            if (wordEntity != null) {
                emit(Result.success(wordEntity))
            } else {
                emit(Result.failure(Exception("Word not found in database")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    // Insert a word into the local database
    fun insertWord(wordEntity: WordEntity) = viewModelScope.launch {
        try {
            repository.insertWord(wordEntity)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Delete a word from the local database
    fun deleteWord(word: String) = viewModelScope.launch {
        try {
            repository.deleteWord(word)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    class Factory(private val repository: WordRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(WordViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return WordViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}