package com.cipta.ciptajagonyawifi.ui.article

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cipta.ciptajagonyawifi.model.Article
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ArticleViewModel : ViewModel() {

    private val _articles = MutableStateFlow<List<Article>>(emptyList())
    val articles: StateFlow<List<Article>> = _articles

    init {
        // Inisialisasi untuk mengambil semua artikel
        fetchAllArticles()
    }

    // Fungsi untuk mengambil semua artikel
    private fun fetchAllArticles() {
        viewModelScope.launch {
            _articles.value = ArticleRepository.getAllArticles()
        }
    }

    // Fungsi untuk mengambil artikel berdasarkan ID
    fun fetchArticleById(articleId: String, onResult: (Article?) -> Unit) {
        viewModelScope.launch {
            // Panggil repository untuk mendapatkan artikel berdasarkan ID
            val article = ArticleRepository.getArticleById(articleId)
            onResult(article)
        }
    }
}
