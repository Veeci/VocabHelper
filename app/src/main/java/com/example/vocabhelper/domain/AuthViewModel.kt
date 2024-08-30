package com.example.vocabhelper.domain

import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val _fullName = MutableLiveData<String>()
    val fullName: LiveData<String> get() = _fullName

    private val _profilePicUrl = MutableLiveData<String>()
    val profilePicUrl: LiveData<String> get() = _profilePicUrl

    fun setProfilePicUrl(url: String) {
        _profilePicUrl.value = url
    }

    private val keyGenerator = KeyGenerator.getInstance("AES").apply { init(256) }
    private val secretKey: SecretKey = keyGenerator.generateKey()
    private val ivParameterSpec = IvParameterSpec(ByteArray(16))


    fun uploadProfileImage(
        imageUri: Uri,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val userId = auth.currentUser?.uid ?: return
        val storageRef = FirebaseStorage.getInstance().reference.child("profile_images/$userId.jpg")

        viewModelScope.launch(Dispatchers.IO) {
            try {
                storageRef.putFile(imageUri).await()
                val downloadUrl = storageRef.downloadUrl.await().toString()
                updateProfilePicture(downloadUrl, onSuccess, onFailure)
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onFailure(e)
                }
            }
        }
    }

    private fun updateProfilePicture(
        downloadUrl: String,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val user = auth.currentUser ?: return

        val profileUpdates = UserProfileChangeRequest.Builder()
            .setPhotoUri(Uri.parse(downloadUrl))
            .build()

        user.updateProfile(profileUpdates).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                setProfilePicUrl(downloadUrl)
                updateFirestoreProfilePicUrl(downloadUrl, user.uid, onSuccess, onFailure)
            } else {
                task.exception?.let {
                    onFailure(it)
                }
            }
        }
    }

    private fun updateFirestoreProfilePicUrl(
        downloadUrl: String,
        userId: String,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val profileDocumentRef = db.collection("USERS")
            .document(userId)
            .collection("PROFILE")
            .document("PROFILE PIC")

        profileDocumentRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    profileDocumentRef.update("profilePicUrl", downloadUrl)
                        .addOnSuccessListener {
                            _profilePicUrl.value = downloadUrl
                            onSuccess(downloadUrl)
                        }
                        .addOnFailureListener {
                            onFailure(it)
                        }
                } else {
                    val user = hashMapOf("profilePicUrl" to downloadUrl)
                    profileDocumentRef.set(user, SetOptions.merge())
                        .addOnSuccessListener {
                            onSuccess(downloadUrl)
                        }
                        .addOnFailureListener {
                            onFailure(it)
                        }
                }
            }
            .addOnFailureListener {
                onFailure(it)
            }
    }

    fun signUp(
        email: String,
        password: String,
        fullname: String,
        onSuccess: (FirebaseUser) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val trimmedEmail = email.trim()
                val result = auth.createUserWithEmailAndPassword(trimmedEmail, password).await()
                result.user?.let { user ->
                    val userData = hashMapOf(
                        "email" to trimmedEmail,
                        "fullname" to fullname,
                        "createdAt" to System.currentTimeMillis()
                    )
                    db.collection("USERS")
                        .document(user.uid)
                        .collection("PROFILE")
                        .document(fullname)
                        .set(userData, SetOptions.merge())
                        .await()

                    _fullName.postValue(fullname)
                    withContext(Dispatchers.Main) {
                        onSuccess(user)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onFailure(e)
                }
            }
        }
    }

    fun verifyEmail(user: FirebaseUser, onSuccess: () -> Unit, onFailure: () -> Unit) {
        if (!user.isEmailVerified) {
            user.sendEmailVerification().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onFailure()
                }
            }
        }
    }

    fun firebaseAuthWithGoogle(
        account: GoogleSignInAccount,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                val result = auth.signInWithCredential(credential).await()
                result.user?.let { user ->
                    withContext(Dispatchers.Main) {
                        onSuccess()
                    }
                } ?: withContext(Dispatchers.Main) {
                    onFailure()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    onFailure()
                }
            }
        }
    }

    fun signIn(email: String, password: String, onSuccess: () -> Unit, onFailure: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val trimmedEmail = email.trim()
                val result = auth.signInWithEmailAndPassword(trimmedEmail, password).await()
                result.user?.let { user ->
                    if (user.isEmailVerified) {
                        withContext(Dispatchers.Main) {
                            onSuccess()
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            onFailure()
                        }
                    }
                } ?: withContext(Dispatchers.Main) {
                    onFailure()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    onFailure()
                }
            }
        }
    }

    fun sendPasswordResetEmail(
        email: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    task.exception?.let {
                        onFailure(it)
                    }
                }
            }
    }

    suspend fun encrypt(input: String): String {
        return withContext(Dispatchers.Default) {
            val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
            val iv = ByteArray(16)
            SecureRandom().nextBytes(iv)
            val ivParameterSpec = IvParameterSpec(iv)
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec)
            val encrypted = cipher.doFinal(input.toByteArray())

            Log.d("AuthViewModel", "Secret Key: ${secretKey.encoded.contentToString()}")
            Log.d("AuthViewModel", "IV (Encryption): ${iv.contentToString()}")
            Log.d("AuthViewModel", "Encrypted data (raw): ${encrypted.contentToString()}")

            val ivAndEncrypted = iv + encrypted
            val encoded = Base64.encodeToString(ivAndEncrypted, Base64.NO_WRAP)

            Log.d("AuthViewModel", "Encrypted data (Base64): $encoded")

            encoded
        }
    }

    suspend fun decrypt(input: String): String {
        return withContext(Dispatchers.Default) {
            try {
                Log.d("AuthViewModel", "Decryption - Input (Base64): $input")

                // Decode Base64
                val ivAndEncrypted = Base64.decode(input, Base64.NO_WRAP)
                val iv = ivAndEncrypted.copyOfRange(0, 16)
                val encrypted = ivAndEncrypted.copyOfRange(16, ivAndEncrypted.size)

                Log.d("AuthViewModel", "Decryption - IV: ${iv.contentToString()}")
                Log.d("AuthViewModel", "Decryption - Encrypted Data (raw): ${encrypted.contentToString()}")

                // Initialize cipher for decryption
                val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
                val ivParameterSpec = IvParameterSpec(iv)
                cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec)

                // Decrypt data
                val decrypted = cipher.doFinal(encrypted)
                val decryptedString = String(decrypted)
                Log.d("AuthViewModel", "Decryption - Decrypted Data: $decryptedString")
                decryptedString
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Decryption Error: ${e.message}", e)
                throw e
            }
        }
    }

    fun logOut(
        googleSignInClient: GoogleSignInClient,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                auth.signOut()
                googleSignInClient.signOut().await()
                withContext(Dispatchers.Main) {
                    onSuccess()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onFailure(e)
                }
            }
        }
    }
}