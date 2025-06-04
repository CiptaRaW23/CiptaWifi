package com.cipta.ciptajagonyawifi.ui.admin

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cipta.ciptajagonyawifi.data.UserPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val userPreferences = UserPreferences(context)

    var isLoading by mutableStateOf(false)
        private set

    var registerSuccess by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun setError(message: String?) {
        errorMessage = message
    }

    fun register(email: String, password: String, role: String = "user") {
        isLoading = true
        errorMessage = null
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val uid = result.user?.uid
                if (uid != null) {
                    val userData = hashMapOf("role" to role)
                    firestore.collection("users").document(uid).set(userData)
                        .addOnSuccessListener {
                            viewModelScope.launch {
                                userPreferences.setLoggedIn(true)
                            }
                            registerSuccess = true
                        }
                        .addOnFailureListener {
                            errorMessage = "Gagal menyimpan data pengguna."
                        }
                } else {
                    errorMessage = "Registrasi gagal: user tidak ditemukan."
                }
                isLoading = false
            }
            .addOnFailureListener {
                errorMessage = it.message
                isLoading = false
            }
    }
}
