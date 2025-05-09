package com.cipta.ciptajagonyawifi.ui.promo

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.cipta.ciptajagonyawifi.model.Promo
import com.cipta.ciptajagonyawifi.ui.promo.PromoViewModel

@Composable
fun PromoManagementScreen(
    navController: NavController,
    viewModel: PromoViewModel,
    promoId: String? = null // Opsional, untuk mengedit promo
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var isActive by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }


    // Jika promoId ada, ambil promo yang sudah ada dari Firestore
    LaunchedEffect(promoId) {
        if (promoId != null) {
            val promo = viewModel.getPromoById(promoId)
            promo?.let {
                title = it.title
                description = it.description
                imageUrl = it.imageUrl
                isActive = it.isActive
            }
        }
        isLoading = false
    }

    if (isLoading) {
        // Tampilkan loading spinner
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        // UI setelah data dimuat
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = if (promoId != null) "Edit Promo" else "Tambah Promo",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(16.dp))

            // Input untuk judul dan deskripsi
            TextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Judul Promo") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            TextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Deskripsi Promo") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            TextField(
                value = imageUrl,
                onValueChange = { imageUrl = it },
                label = { Text("URL Gambar") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Aktifkan Promo", modifier = Modifier.weight(1f))
                Switch(
                    checked = isActive,
                    onCheckedChange = {
                        isActive = it
                        println("Promo isActive: $isActive") // Debugging untuk memastikan nilai berubah
                    }
                )
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    if (promoId != null) {
                        // Jika sedang mengedit, perbarui data promo
                        viewModel.updatePromo(promoId, title, description, imageUrl, isActive)
                    } else {
                        // Jika menambah promo baru
                        viewModel.addPromo(
                            Promo(
                                title = title,
                                description = description,
                                imageUrl = imageUrl,
                                isActive = isActive
                            )
                        )
                    }
                    navController.popBackStack() // Navigasi kembali setelah simpan
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Simpan")
            }
        }
    }
}
