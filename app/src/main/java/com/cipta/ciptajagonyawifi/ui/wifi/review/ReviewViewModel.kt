package com.cipta.ciptajagonyawifi.ui.wifi.review

import androidx.lifecycle.ViewModel
import com.cipta.ciptajagonyawifi.model.Review
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ReviewViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val _reviews = MutableStateFlow<List<Review>>(emptyList())
    val reviews: StateFlow<List<Review>> = _reviews

    // Ambil review berdasarkan packageId
    fun fetchReviews(packageId: Int) {
        db.collection("reviews")
            .whereEqualTo("packageId", packageId)
            .orderBy("timestamp")
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener

                val reviewList = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Review::class.java)?.copy(id = doc.id)
                }
                _reviews.value = reviewList
            }
    }

    // Tambah review baru
    fun addReview(packageId: Int, name: String, comment: String, rating: Float) {
        val newReview = Review(
            packageId = packageId,
            name = name,
            comment = comment,
            rating = rating,
            timestamp = System.currentTimeMillis()
        )

        db.collection("reviews")
            .add(newReview)
    }
}