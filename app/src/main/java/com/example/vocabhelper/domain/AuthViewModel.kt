package com.example.vocabhelper.domain

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vocabhelper.presentation.auth.AuthActivity
import com.example.vocabhelper.presentation.main.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    companion object {
        const val REQUEST_CODE_PICK_IMAGE = 1001
    }

    private val _profilePicUrl = MutableLiveData<String>()
    val profilePicUrl: LiveData<String> get() = _profilePicUrl

    fun setProfilePicUrl(url: String) {
        _profilePicUrl.value = url
    }

    // Opens the gallery to pick an image
    fun openGallery(activity: Activity) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        activity.startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
    }

    // Uploads the selected image to Firebase Storage and updates profile picture URL
    fun uploadProfileImage(imageUri: Uri, context: Context) {
        val userId = auth.currentUser?.uid ?: return
        val storageRef = FirebaseStorage.getInstance().reference.child("profile_images/$userId.jpg")

        viewModelScope.launch(Dispatchers.IO) {
            try {
                storageRef.putFile(imageUri).await()
                val downloadUrl = storageRef.downloadUrl.await().toString()
                updateProfilePicture(downloadUrl, context)
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Failed to upload image", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Updates the profile picture URL in Firebase Authentication and Firestore
    private fun updateProfilePicture(downloadUrl: String, context: Context) {
        val user = auth.currentUser ?: return

        val profileUpdates = UserProfileChangeRequest.Builder()
            .setPhotoUri(Uri.parse(downloadUrl))
            .build()

        user.updateProfile(profileUpdates).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                setProfilePicUrl(downloadUrl)
                updateFirestoreProfilePicUrl(downloadUrl, user.uid, context)
            } else {
                Toast.makeText(context, "Failed to update profile picture", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Updates or creates the profile document in Firestore with the new profile picture URL
    private fun updateFirestoreProfilePicUrl(downloadUrl: String, userId: String, context: Context) {
        val profileDocumentRef = db.collection("USERS")
            .document(userId)
            .collection("PROFILE")
            .document("PROFILE PIC")

        profileDocumentRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    profileDocumentRef.update("profilePicUrl", downloadUrl)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Profile picture updated successfully", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Failed to update Firestore", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    // Document does not exist, create it with the profilePicUrl field
                    val user = hashMapOf("profilePicUrl" to downloadUrl)
                    profileDocumentRef.set(user, SetOptions.merge())
                        .addOnSuccessListener {
                            Toast.makeText(context, "Profile picture added successfully", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Failed to add profile picture", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to retrieve profile document", Toast.LENGTH_SHORT).show()
            }
    }

    // Signs up a new user and stores their information in Firestore
    fun signUp(email: String, password: String, fullname: String, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val trimmedEmail = email.trim()
                val result = auth.createUserWithEmailAndPassword(trimmedEmail, password).await()
                if (result.user != null) {
                    val user = hashMapOf(
                        "email" to trimmedEmail,
                        "fullname" to fullname,
                        "createdAt" to System.currentTimeMillis()
                    )
                    db.collection("USERS")
                        .document(result.user!!.uid)
                        .collection("PROFILE")
                        .document(fullname)
                        .set(user)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Sign up successfully", Toast.LENGTH_SHORT).show()
                            verifyEmail(result.user!!, context)
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(context, "Sign up failed", Toast.LENGTH_SHORT).show()
                            e.printStackTrace()
                        }
                        .await()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Sends a verification email to the user
    private fun verifyEmail(user: FirebaseUser, context: Context) {
        user.sendEmailVerification().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Verification email sent! Check your email to log in", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, "Failed to send verification email", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Signs in a user with Google credentials
    fun FirebaseAuthWithGoogle(account: GoogleSignInAccount, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                val result = auth.signInWithCredential(credential).await()
                if (result.user != null) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Google Sign in successful", Toast.LENGTH_SHORT).show()
                        val intent = Intent(context, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        context.startActivity(intent)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Google Sign in failed", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Google Sign in failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Signs in a user with email and password
    fun signIn(email: String, password: String, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val trimmedEmail = email.trim()
                if (auth.currentUser != null) {
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                } else {
                    val result = auth.signInWithEmailAndPassword(trimmedEmail, password).await()
                    if (result.user != null) {
                        if (result.user!!.isEmailVerified) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "Sign in successful", Toast.LENGTH_SHORT).show()
                                val intent = Intent(context, MainActivity::class.java)
                                context.startActivity(intent)
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "Please verify your email", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Invalid email or password", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Signs out the user from both Firebase Auth and Google Sign-In
    fun logOut(context: Context, googleSignInClient: GoogleSignInClient) {
        viewModelScope.launch {
            auth.signOut()
            googleSignInClient.signOut().await()
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Sign out successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(context, AuthActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                context.startActivity(intent)
            }
        }
    }
}
