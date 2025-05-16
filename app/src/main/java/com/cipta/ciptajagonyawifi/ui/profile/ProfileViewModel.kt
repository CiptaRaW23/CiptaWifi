package com.cipta.ciptajagonyawifi.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.cipta.ciptajagonyawifi.data.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _name = userPreferences.name.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), "")
    private val _phone = userPreferences.phone.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), "")
    private val _address = userPreferences.address.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), "")
    private val _avatarUri = userPreferences.avatarUri.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), "")

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
        viewModelScope.launch {
            userPreferences.logout()
            navController.navigate("login") {
                popUpTo("user_home") { inclusive = true } // Hapus dari back stack
            }
        }
    }
}
