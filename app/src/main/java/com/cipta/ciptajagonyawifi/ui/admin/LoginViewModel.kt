package com.cipta.ciptajagonyawifi.ui.admin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    var isLoading by mutableStateOf(false)
        private set

    var loginSuccess by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun login(email: String, password: String) {
        isLoading = true
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val uid = result.user?.uid
                if (uid != null) {
                    // Cek role admin di Firestore
                    firestore.collection("users").document(uid).get()
                        .addOnSuccessListener { doc ->
                            if (doc.getString("role") == "admin") {
                                loginSuccess = true
                            } else {
                                errorMessage = "Akun ini bukan admin."
                            }
                            isLoading = false
                        }
                } else {
                    errorMessage = "Login gagal: user tidak ditemukan"
                    isLoading = false
                }
            }
            .addOnFailureListener {
                errorMessage = it.message
                isLoading = false
            }
    }
}
