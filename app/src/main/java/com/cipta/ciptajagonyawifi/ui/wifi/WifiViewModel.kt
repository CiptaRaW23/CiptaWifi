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
                        doc.toObject(WifiPackage::class.java)?.copy(docId = doc.id)
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
                val docRef = wifiCollection.add(wifiPackage).await()
                val updatedPackage = wifiPackage.copy(docId = docRef.id)
                docRef.set(updatedPackage).await() // update isi docId dalam dokumen
            } catch (e: Exception) {
                Log.e("WifiViewModel", "Error adding wifi package", e)
            }
        }
    }

    fun updateWifiPackage(wifiPackage: WifiPackage) {
        viewModelScope.launch {
            try {
                if (wifiPackage.docId.isNotEmpty()) {
                    wifiCollection.document(wifiPackage.docId).set(wifiPackage).await()
                } else {
                    Log.e("WifiViewModel", "Error: docId is empty for update")
                }
            } catch (e: Exception) {
                Log.e("WifiViewModel", "Error updating wifi package", e)
            }
        }
    }

    fun deleteWifiPackage(docId: String) {
        viewModelScope.launch {
            try {
                if (docId.isNotEmpty()) {
                    wifiCollection.document(docId).delete().await()
                }
            } catch (e: Exception) {
                Log.e("WifiViewModel", "Error deleting wifi package", e)
            }
        }
    }
}
