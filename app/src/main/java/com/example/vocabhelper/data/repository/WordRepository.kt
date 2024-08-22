package com.example.vocabhelper.data.repository

import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.vocabhelper.data.api.APIService
import com.example.vocabhelper.data.models.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

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
                // Handle failure
            }
    }
}