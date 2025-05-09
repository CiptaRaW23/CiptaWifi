package com.cipta.ciptajagonyawifi.ui.article

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.cipta.ciptajagonyawifi.model.Article
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun ArticleDetailScreen(
    articleId: String,
    navController: NavController,
    articleViewModel: ArticleViewModel = hiltViewModel()
) {
    var article by remember { mutableStateOf<Article?>(null) }

    // Ambil artikel dari ViewModel saat screen pertama kali muncul
    LaunchedEffect(articleId) {
        articleViewModel.fetchArticleById(articleId) { result ->
            article = result
        }
    }

    // UI
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        article?.let {
            Text(text = it.title, style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = it.content, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                painter = rememberAsyncImagePainter(it.imageUrl),
                contentDescription = "Article Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
        } ?: run {
            CircularProgressIndicator()
        }
    }
}

