package com.cipta.ciptajagonyawifi.ui.cctv

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cipta.ciptajagonyawifi.model.CctvPackage
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class CctvViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val cctvCollection = firestore.collection("cctvPackages")

    private val _cctvPackages = MutableStateFlow<List<CctvPackage>>(emptyList())
    val cctvPackages: StateFlow<List<CctvPackage>> get() = _cctvPackages

    init {
        fetchCctvPackages()
    }

    private fun fetchCctvPackages() {
        cctvCollection
            .orderBy("id", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("CctvViewModel", "Listen failed.", error)
                    return@addSnapshotListener
                }
                if (snapshot != null && !snapshot.isEmpty) {
                    val list = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(CctvPackage::class.java)?.copy(docId = doc.id)
                    }
                    _cctvPackages.value = list
                } else {
                    _cctvPackages.value = emptyList()
                }
            }
    }

    fun addCctvPackage(cctvPackage: CctvPackage) {
        viewModelScope.launch {
            try {
                val docRef = cctvCollection.add(cctvPackage).await()
                val updatedPackage = cctvPackage.copy(docId = docRef.id)
                docRef.set(updatedPackage).await() // update isi docId dalam dokumen
            } catch (e: Exception) {
                Log.e("CctvViewModel", "Error adding cctv package", e)
            }
        }
    }

    fun updateCctvPackage(cctvPackage: CctvPackage) {
        viewModelScope.launch {
            try {
                if (cctvPackage.docId.isNotEmpty()) {
                    cctvCollection.document(cctvPackage.docId).set(cctvPackage).await()
                } else {
                    Log.e("CctvViewModel", "Error: docId is empty for update")
                }
            } catch (e: Exception) {
                Log.e("CctvViewModel", "Error updating cctv package", e)
            }
        }
    }

    fun deleteCctvPackage(docId: String) {
        viewModelScope.launch {
            try {
                if (docId.isNotEmpty()) {
                    cctvCollection.document(docId).delete().await()
                }
            } catch (e: Exception) {
                Log.e("CctvViewModel", "Error deleting cctv package", e)
            }
        }
    }
}
