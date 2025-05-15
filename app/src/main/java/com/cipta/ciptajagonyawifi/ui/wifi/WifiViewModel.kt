package com.cipta.ciptajagonyawifi.ui.wifi

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cipta.ciptajagonyawifi.model.WifiPackage
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class WifiViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val collection = firestore.collection("wifiPackages")

    private val _wifiPackages = MutableStateFlow<List<WifiPackage>>(emptyList())
    val wifiPackages: StateFlow<List<WifiPackage>> = _wifiPackages

    init {
        fetchWifiPackages()
    }

    fun loadWifiPackages() {
        viewModelScope.launch {
            try {
                val snapshot = collection.get().await()
                val list = snapshot.documents.mapNotNull { it.toObject(WifiPackage::class.java) }
                _wifiPackages.value = list
            } catch (e: Exception) {
                _wifiPackages.value = emptyList()  // fallback kalau error
            }
        }
    }

    private fun fetchWifiPackages() {
        Firebase.firestore.collection("wifiPackages")
            .get()
            .addOnSuccessListener { result ->
                val list = result.documents.mapNotNull {
                    it.toObject(WifiPackage::class.java)
                }.sortedBy { it.id } // sort berdasarkan ID
                _wifiPackages.value = list
            }
            .addOnFailureListener { e ->
                Log.e("HomeViewModel", "Error fetching wifi packages", e)
            }
    }
}