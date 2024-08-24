package com.example.vocabhelper.data.repository

import android.util.Log
import com.example.vocabhelper.data.api.APIService
import com.example.vocabhelper.data.models.Response
import com.example.vocabhelper.data.models.WordData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class WordRepository(private val apiService: APIService) {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    suspend fun getWordFromAPI(word: String): List<Response> {
        return apiService.getWord(word)
    }

    fun saveWord(
        word: String,
        meaning: String?,
        category: String?,
        audioUrl: String?,
        synonym: String?,
        antonym: String?,
        collocation: String?,
        example: String?
    ) {
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

    suspend fun getWords(): List<WordData> {
        val userId = auth.currentUser?.uid ?: return emptyList()
        val wordList = firestore.collection("USERS")
            .document(userId)
            .collection("WORDS")
            .get()
            .await()

        return wordList.documents.mapNotNull {
            it.toObject(WordData::class.java)
        }
    }

    suspend fun getWordDetail(word: String, onComplete: (List<WordData>) -> Unit) {
        val userId = auth.currentUser?.uid?: return
        firestore.collection("USERS")
            .document(userId) // Use the specific user ID
            .collection("WORDS")
            .whereEqualTo("word", word)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val wordList = mutableListOf<WordData>()
                for (document in querySnapshot) {
                    val wordData = document.toObject(WordData::class.java)
                    wordList.add(wordData)
                }
                onComplete(wordList)
            }
            .addOnFailureListener { exception ->
                Log.e("FirestoreError", "Error fetching word data: ", exception)
                onComplete(emptyList()) // Return an empty list on failure
            }
    }
}