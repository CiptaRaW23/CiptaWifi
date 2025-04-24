package com.cipta.ciptajagonyawifi.ui.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.cipta.ciptajagonyawifi.data.dummyPackages
import com.cipta.ciptajagonyawifi.R
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.cipta.ciptajagonyawifi.ui.font.Poopins

@Composable
fun WifiScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.background), // ganti sesuai nama file background-mu
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Konten di atas background
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(dummyPackages) { pkg ->
                GlowingPackageCard(pkg = pkg, navController = navController)
            }
        }
    }
}


@Composable
fun GlowingPackageCard(pkg: com.cipta.ciptajagonyawifi.model.WifiPackage, navController: NavController) {
    var isPressed by remember { mutableStateOf(false) }

    val backgroundColor by animateColorAsState(
        targetValue = if (isPressed) Color(0xFF26A69A) else Color(0xFF2E7D32),
        label = "Card Color Animation"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                        navController.navigate("detail/${pkg.id}")
                    }
                )
            },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            if (isPressed) Color(0xFF81C784) else Color(0xFF1B5E20), // Gradasi berubah saat ditekan
                            Color(0xFF81C784)
                        ),
                        start = Offset.Zero,
                        end = Offset.Infinite
                    )
                )
                .padding(16.dp)
        ) {
            // Icon WiFi di kanan
            Image(
                painter = painterResource(id = R.drawable.ic_wifi2),
                contentDescription = "icon",
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.CenterEnd)
            )

            // Konten teks di kiri
            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
            ) {
                Text(
                    text = pkg.name,
                    fontFamily = Poopins,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
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
