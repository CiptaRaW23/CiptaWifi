package com.cipta.ciptajagonyawifi.ui.promo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.cipta.ciptajagonyawifi.model.Promo
import kotlinx.coroutines.launch

@Composable
fun PromoDetailScreen(
    promoId: String?,
    navController: NavController,
    viewModel: PromoViewModel = viewModel()
) {
    // Jika promoId null, langsung kembali
    if (promoId == null) {
        LaunchedEffect(Unit) {
            navController.popBackStack()
        }
        return
    }

    var promo by remember { mutableStateOf<Promo?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(promoId) {
        isLoading = true
        coroutineScope.launch {
            val fetchedPromo = viewModel.getPromoById(promoId)
            promo = fetchedPromo
            isLoading = false
        }
    }

    val brushBackground = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF1B5E20),
            Color(0xFFFFEE58)
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = promo?.title ?: "Detail Promo",
                        style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Kembali",
                            tint = Color.White
                        )
                    }
                },
                backgroundColor = Color(0xFF1B5E20),
                contentColor = Color.White
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(brush = brushBackground)
        ) {
            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.White)
                    }
                }
                promo != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(bottom = 72.dp)
                    ) {
                        AsyncImage(
                            model = promo?.imageUrl,
                            contentDescription = "Gambar Promo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp)
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = promo?.title ?: "Judul Promo",
                                style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold),
                                color = Color.White,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Text(
                                text = promo?.description ?: "Deskripsi promo tidak tersedia.",
                                style = MaterialTheme.typography.body1,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }
                    }

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Button(
                            onClick = {
                                navController.navigate("form/promo/$promoId")
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(0xFF1B5E20),
                                contentColor = Color.White
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .height(50.dp)
                        ) {
                            Text("Beli Sekarang", fontSize = 16.sp)
                        }
                    }
                }
                else -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Promo tidak ditemukan.",
                            color = Color.White,
                            style = MaterialTheme.typography.h6
                        )
                    }
                }
            }
        }
    }
}
