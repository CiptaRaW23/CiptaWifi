package com.cipta.ciptajagonyawifi.ui.promo

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.cipta.ciptajagonyawifi.model.Promo
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class PromoViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    val promos = mutableStateListOf<Promo>()

    init {
        getPromos()
    }

    fun getPromos() {
        firestore.collection("promos")
            .get()
            .addOnSuccessListener { result ->
                promos.clear()
                for (document in result) {
                    val promo = document.toObject(Promo::class.java).copy(id = document.id)
                    promos.add(promo)
                }
            }
    }

    fun addPromo(promo: Promo) {
        firestore.collection("promos").add(promo)
            .addOnSuccessListener {
                getPromos()
            }
    }

    fun updatePromo(id: String, title: String, description: String, imageUrl: String, isActive: Boolean) {
        val updatedPromo = mapOf(
            "title" to title,
            "description" to description,
            "imageUrl" to imageUrl,
            "isActive" to isActive
        )
        firestore.collection("promos").document(id)
            .set(updatedPromo) // overwrite full
            .addOnSuccessListener {
                getPromos() // Refresh list
            }
        Log.d("PromoViewModel", "updatePromo: isActive = $isActive")
    }


    fun deletePromo(promoId: String) {
        firestore.collection("promos")
            .document(promoId)
            .delete()
            .addOnSuccessListener {
                getPromos()
            }
    }

    suspend fun getPromoById(promoId: String): Promo? {
        return withContext(Dispatchers.IO) {
            val doc = firestore.collection("promos").document(promoId).get().await()
            val promo = doc.toObject(Promo::class.java)
            promo?.copy(id = doc.id) // penting agar id tidak null
        }
    }
}
