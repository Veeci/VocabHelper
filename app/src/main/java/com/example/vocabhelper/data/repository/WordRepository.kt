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
    val userId = auth.currentUser?.uid!!

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
            .document(word)
            .set(wordData)
            .addOnSuccessListener {

            }
            .addOnFailureListener { e ->

            }
    }

    fun updateWord(word: String, updatedData: Map<String, Any>) {
        firestore.collection("USERS")
            .document(userId)
            .collection("WORDS")
            .document(word)
            .update(updatedData)
            .addOnSuccessListener {
                Log.d("Firestore", "Updated data successfully!")
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error updating data", e)
            }
    }

    fun deleteWord(word: String)
    {
        firestore.collection("USERS")
            .document(userId)
            .collection("WORDS")
            .document(word)
            .delete()
            .addOnSuccessListener {
                Log.d("Firestore", "Deleted data successfully!")
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error deleting data", e)
            }
    }

    suspend fun getWords(): List<WordData> {
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
        firestore.collection("USERS")
            .document(userId)
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
                onComplete(emptyList())
            }
    }

    suspend fun getWordsByCategory(category: String): List<WordData> {
        val wordList = firestore.collection("USERS")
            .document(userId)
            .collection("WORDS")
            .whereEqualTo("category", category)
            .get()
            .await()

        return wordList.documents.mapNotNull {
            it.toObject(WordData::class.java)
        }
    }
}