@file:OptIn(ExperimentalPagerApi::class)

package com.cipta.ciptajagonyawifi.ui.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import coil.compose.AsyncImage
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cipta.ciptajagonyawifi.R
import com.google.accompanist.pager.*
import kotlinx.coroutines.delay
import com.cipta.ciptajagonyawifi.model.Article
import com.cipta.ciptajagonyawifi.model.Promo
import androidx.compose.ui.platform.LocalContext
import com.cipta.ciptajagonyawifi.data.UserPreferences
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


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
            .background(Color(0xFFF5F5F5)) // Latar belakang mint muda
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Beranda",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF1B5E20)
            )

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
                    tint = Color(0xFF2E7D32),
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        PromoSlider(promos = promos)

        Spacer(modifier = Modifier.height(16.dp))

        MenuGrid(navController = navController)

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Seputar Edukasi",
            style = MaterialTheme.typography.titleMedium.copy(color = Color(0xFF388E3C)),
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        NewsSection(navController = navController, article = articles)
    }
}

@Composable
fun PromoSlider(promos: List<Promo>) {
    if (promos.isEmpty()) return

    val pagerState = rememberPagerState(initialPage = 0)

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
                .height(180.dp)
        ) { page ->
            AsyncImage(
                model = promos[page].imageUrl,
                contentDescription = "Promo ${promos[page].id}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .fillMaxSize()
                    .aspectRatio(16f / 9f)
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
fun NewsSection(navController: NavController, article: List<Article>) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .heightIn(min = 100.dp, max = 300.dp)
            .verticalScroll(rememberScrollState())
    ) {
        article.forEach { article ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable {
                        navController.navigate("article/${article.id}")
                    },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9)) // Card hijau lembut
            ) {
                Row {
                    AsyncImage(
                        model = article.imageUrl,
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
                        color = Color(0xFF33691E) // Judul artikel
                    )
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
