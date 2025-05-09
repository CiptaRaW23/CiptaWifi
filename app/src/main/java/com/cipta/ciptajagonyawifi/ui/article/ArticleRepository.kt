package com.cipta.ciptajagonyawifi.ui.article

import com.cipta.ciptajagonyawifi.model.Article
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object ArticleRepository {

    private val firestore = FirebaseFirestore.getInstance()

    // Fungsi untuk mengambil artikel berdasarkan ID dari Firestore
    suspend fun getArticleById(id: String): Article? {
        return try {
            // Ambil artikel berdasarkan ID dari koleksi "articles"
            val snapshot = firestore.collection("articles")
                .document(id)
                .get()
                .await()

            // Jika artikel ditemukan, kembalikan sebagai objek Article
            snapshot.toObject(Article::class.java)
        } catch (e: Exception) {
            // Jika terjadi error, kembalikan null
            null
        }
    }

    // Fungsi untuk mengambil semua artikel dari Firestore
    suspend fun getAllArticles(): List<Article> {
        return try {
            val snapshot = firestore.collection("articles")
                .get()
                .await()

            // Mengubah dokumen menjadi objek Article
            snapshot.documents.mapNotNull { it.toObject(Article::class.java)?.copy(id = it.id) }
        } catch (e: Exception) {
            emptyList() // Mengembalikan list kosong jika terjadi error
        }
    }
}
