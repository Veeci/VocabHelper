package com.example.vocabhelper.data.repository

import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.vocabhelper.data.api.APIService
import com.example.vocabhelper.data.models.Response
import com.example.vocabhelper.data.models.WordData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class WordRepository(private val apiService: APIService) {

    private val firestore = FirebaseFirestore.getInstance()

    suspend fun getWordFromAPI(word: String): List<Response> {
        return apiService.getWord(word)
    }

    fun saveWord(word: String, meaning: String?, category: String?, audioUrl: String?, synonym: String?, antonym: String?, collocation: String?, example: String?) {
        val wordData = hashMapOf(
            "word" to word,
            "meaning" to meaning,
            "category" to category,
            "audioUrl" to audioUrl,
            "synonym" to synonym,
            "antonym" to antonym,
            "collocation" to collocation,
            "example" to example
        )

        firestore.collection("USERS")
            .document(FirebaseAuth.getInstance().currentUser?.uid ?: "")
            .collection("WORDS")
            .add(wordData)
            .addOnSuccessListener {

            }
            .addOnFailureListener {

            }
    }

    suspend fun getWords(): List<WordData>{
        val userId = FirebaseAuth.getInstance().currentUser?.uid?: return emptyList()
        val wordList = firestore.collection("USERS")
            .document(userId)
            .collection("WORDS")
            .get()
            .await()

        return wordList.documents.mapNotNull {
            it.toObject(WordData::class.java)
        }
    }
}