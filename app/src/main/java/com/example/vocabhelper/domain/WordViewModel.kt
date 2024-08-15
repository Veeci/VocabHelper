package com.example.vocabhelper.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.vocabhelper.data.repository.WordRepository
import kotlinx.coroutines.Dispatchers

class WordViewModel(private val repository: WordRepository) : ViewModel() {

    fun getWordDefinition(word: String) = liveData(Dispatchers.IO) {
        try {
            val response = repository.getWord(word)
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    class WordViewModelFactory(private val repository: WordRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(WordViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return WordViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
