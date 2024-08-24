package com.example.vocabhelper.domain

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.vocabhelper.data.models.Response
import com.example.vocabhelper.data.models.WordData
import com.example.vocabhelper.data.repository.WordRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class WordViewModel(private val repository: WordRepository) : ViewModel() {

    var wordToAdd: String = ""
    var meaning: String? = null
    var audioUrl: String? = null
    var category: String? = null
    var synonym: String? = null
    var antonym: String? = null
    var collocation: String? = null
    var example: String? = null

    private val _wordRemember = MutableLiveData<String>()
    val wordRemember: LiveData<String> get() = _wordRemember

    fun setWordRemember(word: String) {
        _wordRemember.value = word
    }

    private val _word = MutableLiveData<Response>()
    val word: MutableLiveData<Response> get() = _word

    private val _wordStored = MutableLiveData<List<WordData>>()
    val wordStored: MutableLiveData<List<WordData>> get() = _wordStored

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
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {
            setDataSource(audioUrl)
            prepareAsync()
            setOnPreparedListener {
                it.start()
            }
            setOnCompletionListener {
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


    fun saveWord() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveWord(
                wordToAdd, meaning, category, audioUrl, synonym, antonym, collocation, example
            )
        }
    }

    fun fetchWords() {
        viewModelScope.launch(Dispatchers.IO) {
            val wordStored = repository.getWords()
            _wordStored.postValue(wordStored)
        }
    }

    fun getWordDetail(word: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getWordDetail(word) { wordList ->
                _wordStored.postValue(wordList)
            }
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