package com.veeci.vocabhelper.domain

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainViewModel: ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val _fullName = MutableLiveData<String?>()
    val fullName: LiveData<String?> get() = _fullName

    private val _profilePicUrl = MutableLiveData<String?>()
    val profilePicUrl: LiveData<String?> get() = _profilePicUrl

    fun fetchUserProfile(context: Context) {
        val userId = auth.currentUser!!.uid
        val profilePicRef = db.collection("USERS")
            .document(userId)
            .collection("PROFILE")
            .document("PROFILE PIC")

        profilePicRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val profilePicUrl = document.getString("profilePicUrl")
                    profilePicUrl?.let {
                        _profilePicUrl.postValue(profilePicUrl)
                    }
                } else {
                    val defaultPicUrl = auth.currentUser?.photoUrl?.toString()
                    _profilePicUrl.postValue(defaultPicUrl?: "")
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to retrieve profile", Toast.LENGTH_SHORT).show()
            }
    }
}