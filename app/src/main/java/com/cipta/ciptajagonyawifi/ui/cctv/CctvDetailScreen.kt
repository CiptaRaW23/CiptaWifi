package com.cipta.ciptajagonyawifi.ui.cctv

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cipta.ciptajagonyawifi.R
import com.cipta.ciptajagonyawifi.model.CctvPackage
import com.cipta.ciptajagonyawifi.ui.wifi.review.ReviewSection
import com.google.accompanist.pager.*

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CctvDetailScreen(
    packageId: Int,
    navController: NavController,
    viewModel: CctvViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val cctvPackages by viewModel.cctvPackages.collectAsState()

    val pkg = cctvPackages.find { it.id == packageId }

    if (pkg == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1B5E20)), // Background saat loading
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    val recommendations = remember(packageId) {
        cctvPackages.filter { it.id != packageId }.shuffled().take(3)
    }

    val images = pkg.imageUrls
    if (images.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Text("Tidak ada gambar untuk paket ini", color = Color.Gray)
        }
    }

    val pagerState = rememberPagerState()
    var scale by remember { mutableStateOf(1f) }
    var isZoomed by remember { mutableStateOf(false) }

    val maxScale = 3f
    val minScale = 1f
    val zoomPadding = 16.dp

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 72.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)) {

                HorizontalPager(
                    count = images.size,
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(images[page])
                            .crossfade(true)
                            .error(R.drawable.ic_image_error)
                            .placeholder(R.drawable.ic_image_placeholder)
                            .build(),
                        contentDescription = "Foto CCTV ${pkg.name} ${page + 1}",
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onDoubleTap = {
                                        scale = if (scale == minScale) 2.5f else minScale
                                        isZoomed = scale != minScale
                                    }
                                )
                            },
                        contentScale = ContentScale.Crop
                    )

                }

                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.TopStart)
                        .background(
                            color = Color.White.copy(alpha = 0.7f),
                            shape = RoundedCornerShape(50)
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Kembali",
                        tint = Color.Black
                    )
                }
            }

            HorizontalPagerIndicator(
                pagerState = pagerState,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = pkg.price,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Resolusi: ${pkg.resolution}",
                    fontSize = 16.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Deskripsi:",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = pkg.description,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Fitur:",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = pkg.features,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                ReviewSection(packageId = packageId)

                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Rekomendasi Paket CCTV",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(recommendations) { recommendedPkg ->
                        RecommendationCard(
                            recommendedPkg = recommendedPkg,
                            onClick = { navController.navigate("cctvDetail/${recommendedPkg.id}") }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        Button(
            onClick = { navController.navigate("form/cctv/${pkg.id}") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp)
                .height(50.dp)
        ) {
            Text("Beli Sekarang", color = Color.White, fontSize = 16.sp)
        }

        if (isZoomed) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.85f))
                    .pointerInput(Unit) { detectTapGestures { isZoomed = false } }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(zoomPadding)
                        .align(Alignment.Center)
                ) {
                    AsyncImage(
                        model = images[pagerState.currentPage],
                        contentDescription = "Zoomed Image",
                        contentScale = ContentScale.Inside,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(8.dp))
                            .graphicsLayer {
                                scaleX = scale.coerceIn(minScale, maxScale)
                                scaleY = scale.coerceIn(minScale, maxScale)
                            }
                            .pointerInput(Unit) {
                                detectTransformGestures { _, _, zoom, _ ->
                                    scale = (scale * zoom).coerceIn(minScale, maxScale)
                                }
                            }
                    )
                }

                IconButton(
                    onClick = { isZoomed = false },
                    modifier = Modifier
                        .padding(24.dp)
                        .align(Alignment.TopEnd)
                ) {
                    Icon(Icons.Default.Close, "Close", tint = Color.White)
                }
            }
        }
    }
}

@Composable
fun RecommendationCard(recommendedPkg: CctvPackage, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val gradientColors = listOf(
        if (isPressed) Color(0xFF81C784) else Color(0xFF1B5E20),
        Color(0xFF81C784)
    )

    Box(
        modifier = Modifier
            .width(200.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .background(
                brush = Brush.verticalGradient(
                    colors = gradientColors,
                    startY = 0f,
                    endY = Float.POSITIVE_INFINITY
                )
            )
            .border(
                width = 1.dp,
                color = Color(0xFF388E3C),
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = recommendedPkg.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Resolusi: ${recommendedPkg.resolution}",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.9f)
            )
            Text(
                text = recommendedPkg.price,
                color = Color(0xFFFFEB3B),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = recommendedPkg.description.take(60) + "...",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.8f),
                maxLines = 2
            )
        }
    }
}
