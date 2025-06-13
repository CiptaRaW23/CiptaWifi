package com.cipta.ciptajagonyawifi.ui.article

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter

@Composable
fun ArticleDashboardScreen(
    navController: NavController,
    articleViewModel: ArticleViewModel = hiltViewModel(),
    onAddArticleClick: () -> Unit,
    onEditArticleClick: (String) -> Unit
) {
    val articles by articleViewModel.articles.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kelola Artikel", color = Color.White) },
                backgroundColor = Color(0xFF1B5E20),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddArticleClick,
                backgroundColor = Color(0xFF1B5E20),
                contentColor = Color.White
            ) {
                Text("+")
            }
        }
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier.fillMaxSize()
        ) {
            items(articles) { article ->
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .clickable { onEditArticleClick(article.id) },
                    elevation = 4.dp
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = rememberImagePainter(article.imageUrl),
                            contentDescription = null,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(text = article.title, style = MaterialTheme.typography.h6)
                            Text(text = article.content.take(50) + "...")
                        }
                    }
                }
            }
        }
    }
}
