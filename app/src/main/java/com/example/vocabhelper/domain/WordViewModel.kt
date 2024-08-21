package com.example.vocabhelper.domain

import android.content.Context
import android.media.MediaPlayer
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.vocabhelper.data.database.WordEntity
import com.example.vocabhelper.data.models.Response
import com.example.vocabhelper.data.repository.WordRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WordViewModel(private val repository: WordRepository) : ViewModel() {

    private val _word = MutableLiveData<Response>()
    val word: MutableLiveData<Response> get() = _word

    private var mediaPlayer: MediaPlayer? = null

    // Fetch word definition from API
    fun getWordDefinition(word: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try{
                val response = repository.getWordFromAPI(word)
                if (response.isNotEmpty()) {
                    _word.postValue(response[0])
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun playAudio(context: Context, audioUrl: String) {
        mediaPlayer?.release() // Release any existing MediaPlayer instance
        mediaPlayer = MediaPlayer().apply {
            setDataSource(audioUrl)
            prepareAsync()
            setOnPreparedListener {
                it.start()
            }
            setOnCompletionListener {
                // Release the MediaPlayer once playback is done
                it.release()
            }
            setOnErrorListener { _, _, _ ->
                Toast.makeText(context, "Failed to play audio", Toast.LENGTH_SHORT).show()
                false
            }
        }
    }

    fun releaseMediaPlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
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