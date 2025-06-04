package com.cipta.ciptajagonyawifi.ui.wifi.review


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cipta.ciptajagonyawifi.ui.wifi.review.ReviewViewModel


@Composable
fun ReviewSection(packageId: Int, reviewViewModel: ReviewViewModel = viewModel()) {
    LaunchedEffect(packageId) {
        reviewViewModel.fetchReviews(packageId)
    }

    val reviews by reviewViewModel.reviews.collectAsState()

    var name by remember { mutableStateOf("") }
    var comment by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf(0f) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Ulasan Pelanggan",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (reviews.isEmpty()) {
            Text("Tidak Ada Ulasan", color = MaterialTheme.colorScheme.onSurfaceVariant)
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                reviews.forEach { review ->
                    ReviewCard(
                        name = review.name,
                        comment = review.comment,
                        rating = review.rating
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Tinggalkan Ulasan",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nama") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = comment,
            onValueChange = { comment = it },
            label = { Text("Ulasan") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text("Rating: ${rating.toInt()} ⭐")
        Slider(
            value = rating,
            onValueChange = { rating = it },
            valueRange = 0f..5f,
            steps = 4
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (name.isNotBlank() && comment.isNotBlank()) {
                    reviewViewModel.addReview(packageId, name, comment, rating)
                    name = ""
                    comment = ""
                    rating = 0f
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Kirim")
        }
    }
}

@Composable
fun ReviewCard(name: String, comment: String, rating: Float) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(name, fontWeight = FontWeight.Bold)
            Text("⭐ ${rating.toInt()}", color = Color(0xFFFFC107))
            Text(comment)
        }
    }
}