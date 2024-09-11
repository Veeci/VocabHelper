package com.example.vocabhelper.domain

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.vocabhelper.data.models.Response
import com.example.vocabhelper.data.models.WordData
import com.example.vocabhelper.data.repository.WordRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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

    private val _word = MutableLiveData<Response>()
    val word: MutableLiveData<Response> get() = _word

    private val _wordStored = MutableLiveData<List<WordData>>()
    val wordStored: MutableLiveData<List<WordData>> get() = _wordStored

    private val _wordDetail = MutableLiveData<List<WordData>>()
    val wordDetail: MutableLiveData<List<WordData>> get() = _wordDetail

    private val _categoryRemember = MutableLiveData<String>()
    val categoryRemember: MutableLiveData<String> get() = _categoryRemember

    fun setCategoryRemember(category: String) {
        _categoryRemember.value = category
    }

    private val _wordsByCategory = MutableLiveData<List<WordData>>()
    val wordsByCategory: MutableLiveData<List<WordData>> get() = _wordsByCategory

    private var mediaPlayer: MediaPlayer? = null

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    val userId: String? = auth.currentUser?.uid

    val message = MutableLiveData<String>()

    fun getWordDefinition(word: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
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
            userId?.let { userId ->
                val wordData = hashMapOf(
                    "word" to wordToAdd,
                    "meaning" to meaning,
                    "audioUrl" to audioUrl,
                    "category" to category,
                    "synonym" to synonym,
                    "antonym" to antonym,
                    "collocation" to collocation,
                    "example" to example
                )

                firestore.collection("USERS")
                    .document(userId)
                    .collection("WORDS")
                    .document(wordToAdd)
                    .set(wordData)
                    .addOnSuccessListener {
                        Log.d("WordViewModel", "Added word to Firebase successfully!")
                        message.postValue("Added new word successfully!")
                        fetchWords()
                    }
                    .addOnFailureListener { e ->
                        Log.w("WordViewModel", "Error adding word to Firebase", e)
                        message.postValue("Failed to add new word!")
                    }
            }
        }
    }

    fun fetchWords() {
        viewModelScope.launch(Dispatchers.IO) {
            val wordList = userId?.let {
                firestore.collection("USERS")
                    .document(it)
                    .collection("WORDS")
                    .get()
                    .await()
            }

            val words = wordList?.documents?.mapNotNull { doc ->
                doc.toObject(WordData::class.java)
            } ?: emptyList()

            _wordStored.postValue(words)
        }
    }

    fun getWordDetail(word: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val wordDetail = userId?.let{
                firestore.collection("USERS")
                    .document(it)
                    .collection("WORDS")
                    .whereEqualTo("word", word)
                    .get()
                    .await()
            }

            val word = wordDetail?.documents?.firstNotNullOfOrNull { doc ->
                doc.toObject(WordData::class.java)
            }

            if (word != null) {
                _wordDetail.postValue(listOf(word))
            }
        }
    }

    fun fetchWordsByCategory(category: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val wordsByCategory = userId?.let {
                firestore.collection("USERS")
                    .document(it)
                    .collection("WORDS")
                    .whereEqualTo("category", category)
                    .get()
                    .await()
            }

            val wordList = wordsByCategory?.documents?.mapNotNull { doc ->
                doc.toObject(WordData::class.java)
            } ?: emptyList()

            _wordsByCategory.postValue(wordList)
        }
    }

    fun updateWord(wordData: WordData, updatedData: Map<String, Any>) {
        viewModelScope.launch(Dispatchers.IO) {
            userId?.let {
                firestore.collection("USERS")
                    .document(it)
                    .collection("WORDS")
                    .document(wordData.word)
                    .update(updatedData)
                    .addOnSuccessListener {
                        Log.d("WordViewModel", "Word updated successfully!")
                        message.postValue("This word is successfully updated!")
                        fetchWords()
                    }
                    .addOnFailureListener { e ->
                        message.postValue("There must be some errors when updating this word!")
                        Log.w("WordViewModel", "Error updating word", e)
                    }

            }
        }
    }

    fun deleteWord(wordData: WordData) {
        viewModelScope.launch(Dispatchers.IO) {
            userId?.let {
                firestore.collection("USERS")
                    .document(it)
                    .collection("WORDS")
                    .document(wordData.word)
                    .delete()
                    .addOnSuccessListener {
                        Log.d("WordViewModel", "Word deleted successfully!")
                        message.postValue("This word is successfully deleted!")
                        fetchWords()
                    }
                    .addOnFailureListener { e ->
                        Log.w("WordViewModel", "Error deleting word", e)
                        message.postValue("There must be some errors when deleting this word!")
                    }
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