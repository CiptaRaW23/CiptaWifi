package com.cipta.ciptajagonyawifi.ui.profile

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.cipta.ciptajagonyawifi.data.UserPreferences
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import androidx.compose.runtime.State

import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _name = userPreferences.name.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), "")
    private val _phone = userPreferences.phone.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), "")
    private val _address = userPreferences.address.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), "")
    private val _avatarUri = userPreferences.avatarUri.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), "")
    private val _loginSuccess = mutableStateOf(false)
    private val _userRole = mutableStateOf("")


    val name: StateFlow<String> = _name
    val phone: StateFlow<String> = _phone
    val address: StateFlow<String> = _address
    val avatarUri: StateFlow<String> = _avatarUri



    fun saveProfile(name: String, phone: String, address: String, avatarUri: String) {
        viewModelScope.launch {
            userPreferences.saveProfile(name, phone, address, avatarUri)
        }
    }

    fun logout(navController: NavController) {
        FirebaseAuth.getInstance().signOut()

        viewModelScope.launch {
            userPreferences.clearUserSession()
            _loginSuccess.value = false
            _userRole.value = ""
            navController.navigate("home") {
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
    }
}
