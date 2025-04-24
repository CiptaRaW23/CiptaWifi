@file:OptIn(ExperimentalPagerApi::class)

package com.cipta.ciptajagonyawifi.ui.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.cipta.ciptajagonyawifi.R
import com.cipta.ciptajagonyawifi.data.sampleArticles
import com.google.accompanist.pager.*
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(navController: NavController) {
    val promoImages = listOf(
        R.drawable.background,
        R.drawable.background
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Beranda",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(16.dp),
            color = Color(0xFF1B5E20)
        )

        PromoSlider(images = promoImages)

        Spacer(modifier = Modifier.height(16.dp))

        MenuGrid(navController = navController)

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Seputar Edukasi",
            style = MaterialTheme.typography.titleMedium.copy(color = Color(0xFF388E3C)),
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        NewsSection(navController = navController)

    }
}

@Composable
fun PromoSlider(images: List<Int>) {
    val pagerState = rememberPagerState(initialPage = 0)

    LaunchedEffect(pagerState) {
        while (true) {
            delay(3000L) // Ganti slide setiap 3 detik
            val nextPage = (pagerState.currentPage + 1) % images.size
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Column {
        HorizontalPager(
            count = images.size,
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        ) { page ->
            Image(
                painter = painterResource(id = images[page]),
                contentDescription = "Promo $page",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .fillMaxSize()
            )
        }

        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(8.dp),
            activeColor = Color(0xFF4CAF50)
        )
    }
}

@Composable
fun MenuGrid(navController: NavController) {
    val menuItems = listOf(
        Pair("WiFi", R.drawable.ic_wifi2),
        Pair("CCTV", R.drawable.ic_wifi2),
        Pair("TV", R.drawable.ic_wifi2),
        Pair("Web", R.drawable.ic_wifi2)
    )

    Column(modifier = Modifier.padding(horizontal = 5.dp)) {
        for (i in menuItems.chunked(2)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                i.forEach { (title, icon) ->
                    MenuItem(title = title, icon = icon) {
                        if (title == "WiFi") {
                            navController.navigate("wifiscreen")
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun MenuItem(title: String, icon: Int, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(170.dp)
            .clickable { onClick() }
            .background(Color(0xFFE8F5E9), RoundedCornerShape(12.dp))
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = title,
            tint = Color(0xFF2E7D32),
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF1B5E20)
        )
    }
}

@Composable
fun NewsSection(navController: NavController) {
    Column(modifier = Modifier.padding(16.dp)) {
        sampleArticles.forEach { article ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable {
                        navController.navigate("article/${article.id}")
                    },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9))
            ) {
                Row {
                    Image(
                        painter = painterResource(id = article.imageResId),
                        contentDescription = null,
                        modifier = Modifier
                            .size(64.dp)
                            .padding(8.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = article.title,
                        modifier = Modifier
                            .padding(16.dp)
                            .weight(1f),
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFF33691E)
                    )
                }
            }
        }
    }
}

