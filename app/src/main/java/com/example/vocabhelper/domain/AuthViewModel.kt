package com.example.vocabhelper.domain

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vocabhelper.presentation.auth.AuthActivity
import com.example.vocabhelper.presentation.auth.fragments.LoginFragment
import com.example.vocabhelper.presentation.main.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val _profilePicUrl = MutableLiveData<String>()
    val profilePicUrl: LiveData<String> get() = _profilePicUrl

    fun setProfilePicUrl(url: String) {
        _profilePicUrl.value = url
    }


    fun signUp(email: String, password: String, fullname: String, context: Context) {
        viewModelScope.launch (Dispatchers.IO) {
            try {
                val trimmedEmail = email.trim()
                val result = auth.createUserWithEmailAndPassword(trimmedEmail, password).await()
                if (result.user != null) {
                    val user = hashMapOf(
                        "email" to trimmedEmail,
                        "fullname" to fullname,
                        "createdAt" to System.currentTimeMillis()
                    )
                    db.collection("users")
                        .document(result.user!!.uid)
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

    private fun verifyEmail(user: FirebaseUser, context: Context) {
        user.sendEmailVerification().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Verification email sent! Go to check your email then you can log in", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, "Failed to send verification email", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun FirebaseAuthWithGoogle(account: GoogleSignInAccount, context: Context) {
        viewModelScope.launch (Dispatchers.IO) {
            try {
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                val result = auth.signInWithCredential(credential).await()
                if (result.user != null) {
                    withContext(Dispatchers.Main)
                    {
                        Toast.makeText(context, "Google Sign in successfully", Toast.LENGTH_SHORT).show()
                        val intent = Intent(context, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        context.startActivity(intent)
                    }
                }
                else
                {
                    withContext(Dispatchers.Main)
                    {
                        Toast.makeText(context, "Google Sign in failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            catch (e: Exception)
            {
                e.printStackTrace()
                withContext(Dispatchers.Main)
                {
                    Toast.makeText(context, "Google Sign in failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun signIn(email: String, password: String, context: Context) {
        viewModelScope.launch (Dispatchers.IO) {
            try {
                val trimmedEmail = email.trim()
                if(auth.currentUser != null)
                {
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                }
                else
                {
                    val result = auth.signInWithEmailAndPassword(trimmedEmail, password).await()
                    if (result.user != null) {
                        if(result.user!!.isEmailVerified)
                        {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "Sign in successfully", Toast.LENGTH_SHORT).show()
                                val intent = Intent(context, MainActivity::class.java)
                                context.startActivity(intent)
                            }
                        }
                        else
                        {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "Please verify your email", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    else
                    {
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

    fun logOut(context: Context, googleSignInClient: GoogleSignInClient) {
        viewModelScope.launch {
            auth.signOut()
            googleSignInClient.signOut().await()
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Sign out successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(context, AuthActivity::class.java)
                context.startActivity(intent)
            }
        }
    }
}