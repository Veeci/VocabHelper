package com.example.vocabhelper.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainRepositpory {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    val userId = auth.currentUser?.uid!!

    fun updateUserName()
    {

    }
}