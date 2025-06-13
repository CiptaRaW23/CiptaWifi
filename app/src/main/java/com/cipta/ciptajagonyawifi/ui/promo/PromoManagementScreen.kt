package com.cipta.ciptajagonyawifi.ui.promo

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cipta.ciptajagonyawifi.model.Promo

@Composable
fun PromoManagementScreen(
    navController: NavController,
    viewModel: PromoViewModel,
    promoId: String? = null
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var isActive by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }

    // Load data jika edit
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

    Scaffold(
        topBar = {
            androidx.compose.material.TopAppBar(
                title = {
                    Text(
                        text = if (promoId != null) "Edit Promo" else "Tambah Promo",
                        color = Color.White
                    )
                },
                backgroundColor = Color(0xFF1B5E20),
                navigationIcon = {
                    androidx.compose.material.IconButton(onClick = { navController.popBackStack() }) {
                        androidx.compose.material.Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                // Form input
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
                            println("Promo isActive: $isActive")
                        }
                    )
                }

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (promoId != null) {
                            viewModel.updatePromo(promoId, title, description, imageUrl, isActive)
                        } else {
                            viewModel.addPromo(
                                Promo(
                                    title = title,
                                    description = description,
                                    imageUrl = imageUrl,
                                    isActive = isActive
                                )
                            )
                        }
                        navController.popBackStack()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1B5E20),
                        contentColor = Color.White
                    )
                ) {
                    Text("Simpan")
                }
            }
        }
    }
}
