package com.cipta.ciptajagonyawifi.ui.promo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.cipta.ciptajagonyawifi.model.Promo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PromoDashboardScreen(
    navController: NavController,
    viewModel: PromoViewModel,
    onAddPromoClick: () -> Unit,
    onEditPromoClick: (Promo) -> Unit
) {
    val promos = viewModel.promos
    DisposableEffect(Unit) {
        viewModel.getPromos()
        onDispose { }
    }

    Scaffold(
        topBar = {
            androidx.compose.material.TopAppBar(
                title = { androidx.compose.material.Text("Kelola Promo", color = Color.White) },
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
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("promo_management")
                },
                containerColor = Color(0xFF1B5E20),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Promo")
            }
        }
    ){ padding ->
        LazyColumn(contentPadding = padding) {
            items(promos) { promo ->
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(promo.title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(4.dp))
                        Text(promo.description)
                        Spacer(Modifier.height(4.dp))

                        // Menampilkan gambar promo jika ada
                        if (promo.imageUrl.isNotEmpty()) {
                            AsyncImage(
                                model = promo.imageUrl,
                                contentDescription = "Promo Image",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(150.dp)
                            )
                        }

                        Spacer(Modifier.height(8.dp))
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Tombol Edit
                            Button(
                                onClick = {
                                    // Navigasi ke PromoManagementScreen untuk mengedit promo
                                    navController.navigate("promo_management/${promo.id}")
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4B169D))
                            ) {
                                Text("Edit", color = Color.White)
                            }

                            // Tombol Hapus
                            Button(
                                onClick = {
                                    viewModel.deletePromo(promo.id) // Hapus promo
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                            ) {
                                Text("Hapus", color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}
