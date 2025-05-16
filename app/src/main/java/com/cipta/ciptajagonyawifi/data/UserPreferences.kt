package com.cipta.ciptajagonyawifi.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "user_profile")

class UserPreferences(private val context: Context) {

    companion object {
        val NAME_KEY = stringPreferencesKey("name")
        val PHONE_KEY = stringPreferencesKey("phone")
        val ADDRESS_KEY = stringPreferencesKey("address")
        val AVATAR_URI_KEY = stringPreferencesKey("avatar_uri")
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")

    }

    val name: Flow<String> = context.dataStore.data.map { it[NAME_KEY] ?: "" }
    val phone: Flow<String> = context.dataStore.data.map { it[PHONE_KEY] ?: "" }
    val address: Flow<String> = context.dataStore.data.map { it[ADDRESS_KEY] ?: "" }
    val avatarUri: Flow<String> = context.dataStore.data.map { it[AVATAR_URI_KEY] ?: "" }
    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { it[IS_LOGGED_IN] ?: false }

    suspend fun saveProfile(name: String, phone: String, address: String, avatarUri: String) {
        context.dataStore.edit { prefs ->
            prefs[NAME_KEY] = name
            prefs[PHONE_KEY] = phone
            prefs[ADDRESS_KEY] = address
            prefs[AVATAR_URI_KEY] = avatarUri
        }
    }

    suspend fun setLoggedIn(loggedIn: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[IS_LOGGED_IN] = loggedIn
        }
    }

    suspend fun logout() {
        context.dataStore.edit {
            it.clear()
        }
    }
}
