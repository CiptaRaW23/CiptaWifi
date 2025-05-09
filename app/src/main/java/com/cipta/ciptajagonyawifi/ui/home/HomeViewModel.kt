package com.cipta.ciptajagonyawifi.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import com.cipta.ciptajagonyawifi.model.Article
import com.cipta.ciptajagonyawifi.model.Promo
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {

    // State untuk Article
    private val _articles = MutableStateFlow<List<Article>>(emptyList())
    val articles: StateFlow<List<Article>> = _articles.asStateFlow()

    // State untuk Promo
    private val _promos = MutableStateFlow<List<Promo>>(emptyList())
    val promos: StateFlow<List<Promo>> = _promos.asStateFlow()

    init {
        fetchArticles()
        fetchPromos()
    }

    private fun fetchArticles() {
        Firebase.firestore.collection("articles")
            .get()
            .addOnSuccessListener { result ->
                val list = result.documents.mapNotNull {
                    it.toObject(Article::class.java)?.copy(id = it.id)
                }
                _articles.value = list
            }
            .addOnFailureListener { e ->
                Log.e("HomeViewModel", "Error fetching articles", e)
            }
    }

    private fun fetchPromos() {
        Firebase.firestore.collection("promos")
            .whereEqualTo("isActive", true)
            .get()
            .addOnSuccessListener { result ->
                val list = result.documents.mapNotNull {
                    it.toObject(Promo::class.java)?.copy(id = it.id)
                }
                Log.d("PromoDebug", "Promo list: $list")
                _promos.value = list
            }
            .addOnFailureListener { e ->
                Log.e("HomeViewModel", "Error fetching promos", e)
            }
    }
}
