package com.cipta.ciptajagonyawifi.ui.article

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cipta.ciptajagonyawifi.model.Article
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ArticleViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val articleCollection = db.collection("articles")

    private val _articles = MutableStateFlow<List<Article>>(emptyList())
    val articles: StateFlow<List<Article>> get() = _articles

    // Article detail state
    private val _articleDetail = MutableStateFlow<Article?>(null)
    val articleDetail: StateFlow<Article?> get() = _articleDetail

    init {
        fetchArticles()
    }

    private fun fetchArticles() {
        articleCollection
            .orderBy("timestamp")
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null && !snapshot.isEmpty) {
                    val list = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(Article::class.java)?.copy(id = doc.id)
                    }
                    _articles.value = list
                }
            }
    }

    // Add new article
    fun addArticle(article: Article) {
        articleCollection.add(article)
    }

    // Update article by ID
    fun updateArticle(id: String, title: String, content: String, imageUrl: String) {
        articleCollection.document(id).update(
            mapOf(
                "title" to title,
                "content" to content,
                "imageUrl" to imageUrl
            )
        )
    }

    // Delete article by ID
    fun deleteArticle(id: String) {
        articleCollection.document(id).delete()
    }

    // Fetch article by ID
    suspend fun fetchArticleById(id: String) {
        val article = articleCollection.document(id).get().await().toObject(Article::class.java)
        _articleDetail.value = article
    }

    // Get article by ID (for use in management screen)
    suspend fun getArticleById(id: String): Article? {
        return articleCollection.document(id).get().await().toObject(Article::class.java)?.copy(id = id)
    }
}
