package com.cipta.ciptajagonyawifi.ui.wifi

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
                .padding(16.dp)
        ) {
            Text(
                text = "Paket WiFi",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFFF1F8E9),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(wifiPackages) { pkg ->
                    GlowingPackageCard(pkg = pkg, navController = navController)
                }
            }
        }
    }
}



@Composable
fun GlowingPackageCard(pkg: WifiPackage, navController: NavController) {
    var isPressed by remember { mutableStateOf(false) }

    // Menggunakan clickable dengan interactionSource
    val interactionSource = remember { MutableInteractionSource() }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .clickable(
                indication = null,  // Menghilangkan efek ripple default
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
            // Gambar Icon WiFi di kanan
            Image(
                painter = painterResource(id = R.drawable.ic_wifi2),
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
