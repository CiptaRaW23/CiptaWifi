package com.cipta.ciptajagonyawifi.ui.wifi

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.cipta.ciptajagonyawifi.R
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.cipta.ciptajagonyawifi.model.WifiPackage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WifiScreen(navController: NavController, viewModel: WifiViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val wifiPackages by viewModel.wifiPackages.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.6f),
                            Color.Black.copy(alpha = 0.3f),
                            Color.Transparent
                        )
                    )
                )
        )

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Paket WiFi",
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
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
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = Color.White
                    )
                )
            },
            containerColor = Color.Transparent,
            modifier = Modifier.fillMaxSize()
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(wifiPackages) { pkg ->
                        GlowingPackageCard(pkg = pkg, navController = navController)
                    }
                }
            }
        }
    }
}



@Composable
fun GlowingPackageCard(pkg: WifiPackage, navController: NavController) {
    var isPressed by remember { mutableStateOf(false) }

    val interactionSource = remember { MutableInteractionSource() }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .clickable(
                indication = null,
                interactionSource = interactionSource,
                onClick = {
                    isPressed = true
                    navController.navigate("detail/${pkg.id}")
                }
            ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
        colors = CardDefaults.cardColors(containerColor = if (isPressed) Color(0xFF26A69A) else Color(0xFF2E7D32))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            if (isPressed) Color(0xFF81C784) else Color(0xFF1B5E20),
                            Color(0xFF81C784)
                        ),
                        start = Offset.Zero,
                        end = Offset.Infinite
                    )
                )
                .padding(16.dp)
        ) {

            Image(
                painter = painterResource(id = R.drawable.ic_wifi),
                contentDescription = "icon",
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.CenterEnd)
            )

            // Kolom teks di kiri
            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
            ) {
                Text(
                    text = pkg.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = pkg.speed,
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
                Text(
                    text = pkg.price,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFB9F6CA)
                )
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun WifiScreenPreview() {
    val navController = rememberNavController()
    MaterialTheme {
        WifiScreen(navController = navController)
    }
}
