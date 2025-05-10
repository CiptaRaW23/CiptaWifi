package com.cipta.ciptajagonyawifi.ui.article

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cipta.ciptajagonyawifi.model.Article
import kotlinx.coroutines.launch

@Composable
fun ArticleManagementScreen(
    navController: NavController,
    articleId: String? = null,
    viewModel: ArticleViewModel = androidx.lifecycle.viewmodel.compose.viewModel() // Ubah jadi 'viewModel' sesuai dengan yang dikirim
) {
    val scope = rememberCoroutineScope()

    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }

    LaunchedEffect(articleId) {
        if (!articleId.isNullOrEmpty()) {
            val article = viewModel.getArticleById(articleId) // Ganti articleViewModel menjadi viewModel
            article?.let {
                title = it.title
                content = it.content
                imageUrl = it.imageUrl
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(if (articleId != null) "Edit Artikel" else "Tambah Artikel") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Judul Artikel") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Konten Artikel") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 6
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = imageUrl,
                onValueChange = { imageUrl = it },
                label = { Text("URL Gambar") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (articleId != null) {
                        viewModel.updateArticle(articleId, title, content, imageUrl) // Ganti articleViewModel menjadi viewModel
                    } else {
                        viewModel.addArticle(
                            Article(title = title, content = content, imageUrl = imageUrl)
                        ) // Ganti articleViewModel menjadi viewModel
                    }
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Simpan")
            }

            if (articleId != null) {
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = {
                        scope.launch {
                            viewModel.deleteArticle(articleId) // Ganti articleViewModel menjadi viewModel
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colors.error)
                ) {
                    Text("Hapus")
                }
            }
        }
    }
}
