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
class LoginViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val userPreferences = UserPreferences(context)


    var isLoading by mutableStateOf(false)
        private set

    var loginSuccess by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var userRole by mutableStateOf<String?>(null)
        private set


    fun login(email: String, password: String) {
        isLoading = true
        errorMessage = null
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val uid = result.user?.uid
                if (uid != null) {
                    firestore.collection("users").document(uid).get()
                        .addOnSuccessListener { doc ->
                            val role = doc.getString("role")
                            if (role != null) {
                                userRole = role
                                loginSuccess = true
                                // Simpan status login di DataStore
                                viewModelScope.launch {
                                    userPreferences.setLoggedIn(true)
                                }
                            } else {
                                errorMessage = "Role tidak ditemukan."
                            }
                            isLoading = false
                        }
                        .addOnFailureListener {
                            errorMessage = "Gagal mengambil data user."
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
