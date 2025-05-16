package com.cipta.ciptajagonyawifi.ui.wifi

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cipta.ciptajagonyawifi.model.WifiPackage
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class WifiViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val wifiCollection = firestore.collection("wifiPackages")

    private val _wifiPackages = MutableStateFlow<List<WifiPackage>>(emptyList())
    val wifiPackages: StateFlow<List<WifiPackage>> get() = _wifiPackages

    init {
        fetchWifiPackages()
    }



    private fun fetchWifiPackages() {
        wifiCollection
            .orderBy("id", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("WifiViewModel", "Listen failed.", error)
                    return@addSnapshotListener
                }
                if (snapshot != null && !snapshot.isEmpty) {
                    val list = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(WifiPackage::class.java)
                    }
                    _wifiPackages.value = list
                } else {
                    _wifiPackages.value = emptyList()
                }
            }
    }

    fun addWifiPackage(wifiPackage: WifiPackage) {
        viewModelScope.launch {
            try {
                wifiCollection.add(wifiPackage).await()
            } catch (e: Exception) {
                Log.e("WifiViewModel", "Error adding wifi package", e)
            }
        }
    }

    fun updateWifiPackage(docId: String, wifiPackage: WifiPackage) {
        viewModelScope.launch {
            try {
                wifiCollection.document(docId).set(wifiPackage).await()
            } catch (e: Exception) {
                Log.e("WifiViewModel", "Error updating wifi package", e)
            }
        }
    }

    suspend fun deleteWifiPackage(docId: String) {
        viewModelScope.launch {
            try {
                wifiCollection.document(docId).delete().await()
            } catch (e: Exception) {
                Log.e("WifiViewModel", "Error deleting wifi package", e)
            }
        }
    }
}
