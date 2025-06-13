@file:OptIn(ExperimentalPagerApi::class)

package com.cipta.ciptajagonyawifi.ui.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cipta.ciptajagonyawifi.R
import com.google.accompanist.pager.*
import kotlinx.coroutines.delay
import com.cipta.ciptajagonyawifi.model.Article
import com.cipta.ciptajagonyawifi.model.Promo
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import com.cipta.ciptajagonyawifi.data.UserPreferences


@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = viewModel()) {
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    val isLoggedIn by userPreferences.isLoggedIn.collectAsState(initial = false)
    val articles by viewModel.articles.collectAsState()
    val promos by viewModel.promos.collectAsState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1B5E20),
                        Color(0xFFFFEE58)
                    )
                )
            )
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Beranda",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFFF1F8E9)
                )
            }

            IconButton(
                onClick = {
                    if (isLoggedIn) {
                        navController.navigate("user_home")
                    } else {
                        navController.navigate("login")
                    }
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_user),
                    contentDescription = "Login Admin",
                    tint = Color(0xFFF1F8E9),
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        PromoSlider(promos = promos, navController = navController)

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Layanan Kami",
            style = MaterialTheme.typography.titleMedium.copy(color = Color(0xFFF1F8E9)),
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        MenuGrid(navController = navController)

        Spacer(modifier = Modifier.height(24.dp))

        NewsSection(
            navController = navController,
            title = "Berita Terbaru",
            articles = articles
        )

        Spacer(modifier = Modifier.height(24.dp))

    }
}

@Composable
fun PromoSlider(promos: List<Promo>, navController: NavController) {
    if (promos.isEmpty()) return

    val pagerState = rememberPagerState(initialPage = 0)
    val density = LocalDensity.current

    LaunchedEffect(pagerState) {
        while (true) {
            delay(3000L)
            val nextPage = (pagerState.currentPage + 1) % promos.size
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Column {
        HorizontalPager(
            count = promos.size,
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) { page ->
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .fillMaxSize()
                    .aspectRatio(16f / 9f)
                    .clickable { navController.navigate("promo_detail/${promos[page].id}") }
            ) {
                AsyncImage(
                    model = promos[page].imageUrl,
                    contentDescription = "Promo ${promos[page].id}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f)),
                                startY = 0.5f * density.density,
                                endY = density.density
                            )
                        ),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = promos[page].title,
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                        if (promos[page].description.isNotBlank()) {
                            Text(
                                text = promos[page].description.take(50) + "...",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.8f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
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
        Triple("WiFi", R.drawable.ic_wifi2, "wifiscreen"),
        Triple("CCTV", R.drawable.ic_cctv, "cctvscreen"),
        Triple("TV", R.drawable.background, "tvscreen"),
        Triple("Web", R.drawable.background, "webscreen")
    )

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        for (i in menuItems.chunked(2)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Top
            ) {
                i.forEach { (title, icon, route) ->
                    MenuItem(
                        title = title,
                        icon = icon,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp)
                    ) {
                        navController.navigate(route)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun MenuItem(
    title: String,
    icon: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 12.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = title,
                tint = Color(0xFF1B5E20),
                modifier = Modifier.size(50.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = Color(0xFF1B5E20)
            )
        }
    }
}


@Composable
fun NewsSection(navController: NavController, title: String, articles: List<Article>) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(color = Color(0xFFF1F8E9)),
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(articles) { article ->
                Card(
                    modifier = Modifier
                        .width(176.dp)
                        .clickable { navController.navigate("article/${article.id}") },
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column {
                        AsyncImage(
                            model = article.imageUrl,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                        )
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(
                                text = article.title,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    MaterialTheme {
        HomeScreen(navController = navController)
    }
}
