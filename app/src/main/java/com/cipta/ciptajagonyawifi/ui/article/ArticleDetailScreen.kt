package com.cipta.ciptajagonyawifi.ui.article

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    val scrollState = rememberScrollState()
    var article by remember { mutableStateOf<Article?>(null) }

    LaunchedEffect(articleId) {
        articleViewModel.fetchArticleById(articleId)
    }

    val articleState by articleViewModel.articleDetail.collectAsState()
    article = articleState

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FB))
    ) {
        // Konten artikel
        article?.let {
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .fillMaxSize()
                    .padding(top = 72.dp, start = 16.dp, end = 16.dp, bottom = 16.dp) // ruang untuk top bar
            ) {
                Image(
                    painter = rememberAsyncImagePainter(it.imageUrl),
                    contentDescription = "Article Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.LightGray)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = it.title,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    ),
                    color = Color(0xFF1B1C1E)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = it.content,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        lineHeight = 24.sp,
                        fontSize = 16.sp
                    ),
                    color = Color(0xFF2C2C2C)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Dipublikasikan: ${formatTimestamp(it.timestamp.seconds)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }

        // Custom Top Bar tetap di atas
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(Color.White)
                .align(Alignment.TopCenter),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxSize()
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Kembali",
                        tint = Color(0xFF2E7D32)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Detail Artikel",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    ),
                    color = Color.Black
                )
            }
        }
    }
}
