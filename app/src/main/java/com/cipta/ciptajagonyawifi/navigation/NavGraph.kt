package com.cipta.ciptajagonyawifi.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.cipta.ciptajagonyawifi.ui.promo.PromoDashboardScreen
import com.cipta.ciptajagonyawifi.ui.promo.PromoViewModel
import com.cipta.ciptajagonyawifi.ui.admin.AdminDashboardScreen
import com.cipta.ciptajagonyawifi.ui.admin.LoginScreen
import com.cipta.ciptajagonyawifi.ui.admin.RegisterScreen
import com.cipta.ciptajagonyawifi.ui.admin.UserHomeScreen
import com.cipta.ciptajagonyawifi.ui.article.ArticleDashboardScreen
import com.cipta.ciptajagonyawifi.ui.article.ArticleDetailScreen
import com.cipta.ciptajagonyawifi.ui.article.ArticleManagementScreen
import com.cipta.ciptajagonyawifi.ui.article.ArticleViewModel
import com.cipta.ciptajagonyawifi.ui.cctv.CctvDashboardScreen
import com.cipta.ciptajagonyawifi.ui.cctv.CctvDetailScreen
import com.cipta.ciptajagonyawifi.ui.cctv.CctvManagementScreen
import com.cipta.ciptajagonyawifi.ui.cctv.CctvScreen
import com.cipta.ciptajagonyawifi.ui.cctv.CctvViewModel
import com.cipta.ciptajagonyawifi.ui.wifi.DetailScreen
import com.cipta.ciptajagonyawifi.ui.wifi.FormScreen
import com.cipta.ciptajagonyawifi.ui.home.HomeScreen
import com.cipta.ciptajagonyawifi.ui.promo.PromoDetailScreen
import com.cipta.ciptajagonyawifi.ui.wifi.WifiScreen
import com.cipta.ciptajagonyawifi.ui.promo.PromoManagementScreen
import com.cipta.ciptajagonyawifi.ui.splash.SplashScreen
import com.cipta.ciptajagonyawifi.ui.wifi.WifiDashboardScreen
import com.cipta.ciptajagonyawifi.ui.wifi.WifiManagementScreen
import com.cipta.ciptajagonyawifi.ui.wifi.WifiViewModel


@Composable
fun NavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController, startDestination = "splash", modifier = modifier) {
        composable("splash") {
            SplashScreen(navController)
        }
        composable("home") {
            HomeScreen(navController)
        }
        composable("wifiscreen") {
            WifiScreen(navController)
        }

        composable("cctvscreen") {
            CctvScreen(navController)
        }

        composable("cctvdetail/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: 0
            CctvDetailScreen(packageId = id, navController = navController)
        }


        composable("detail/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: 0
            DetailScreen(packageId = id, navController = navController)
        }

        composable(
            "form/{type}/{packageId}",
            arguments = listOf(
                navArgument("type") { type = NavType.StringType },
                navArgument("packageId") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { backStackEntry ->
            val type = backStackEntry.arguments?.getString("type")
            val packageId = backStackEntry.arguments?.getString("packageId")

            FormScreen(packageId = packageId, type = type, navController = navController)
        }


        composable("article/{articleId}") { backStackEntry ->
            val articleId = backStackEntry.arguments?.getString("articleId")
            articleId?.let {
                ArticleDetailScreen(articleId = it, navController = navController)
            }
        }

        composable("login") {
            LoginScreen(navController = navController)
        }

        composable("register") {
            RegisterScreen(navController)
        }


        composable("admin_dashboard") {
            AdminDashboardScreen(navController = navController)
        }

        composable("user_home") {
            UserHomeScreen(navController = navController)
        }


        composable("article_dashboard") {
            val articleViewModel: ArticleViewModel = hiltViewModel() // Jika menggunakan Hilt
            ArticleDashboardScreen(
                navController = navController,
                articleViewModel = articleViewModel, // Pass viewModel ke composable
                onAddArticleClick = {
                    navController.navigate("article_management")
                },
                onEditArticleClick = { articleId ->
                    navController.navigate("article_management/$articleId")
                }
            )
        }



        // Article Management - Untuk mengelola artikel (tambah/edit)
        composable("article_management") {
            val articleViewModel: ArticleViewModel = hiltViewModel() // Membuat instance ArticleViewModel
            ArticleManagementScreen(
                navController = navController,
                viewModel = articleViewModel,
                articleId = null // Null berarti menambah artikel
            )
        }

        // Untuk edit artikel
        composable("article_management/{articleId}") { backStackEntry ->
            val articleId = backStackEntry.arguments?.getString("articleId")
            val articleViewModel: ArticleViewModel = hiltViewModel()
            ArticleManagementScreen(
                navController = navController,
                viewModel = articleViewModel,
                articleId = articleId // Mengirim articleId untuk edit
            )
        }

        // Promo Dashboard - Menampilkan daftar promo
        composable("promo_dashboard") {
            PromoDashboardScreen(
                navController = navController,
                viewModel = PromoViewModel(), // Ganti sesuai ViewModel Anda
                onAddPromoClick = {
                    // Tangani aksi menambah promo di sini
                    navController.navigate("promo_management")
                },
                onEditPromoClick = { promo ->
                    // Tangani aksi edit promo di sini
                    navController.navigate("promo_management?promoId=${promo.id}")
                }
            )
        }

        // Promo Management - Untuk mengelola promo yang ada
        // Untuk tambah promo
        composable("promo_management") {
            val viewModel: PromoViewModel = viewModel()

            PromoManagementScreen(
                navController = navController,
                viewModel = viewModel,
                promoId = null // Null artinya tambah
            )
        }

        // Untuk edit promo
        composable("promo_management/{promoId}") { backStackEntry ->
            val promoId = backStackEntry.arguments?.getString("promoId")
            val viewModel: PromoViewModel = viewModel()

            PromoManagementScreen(
                navController = navController,
                viewModel = viewModel,
                promoId = promoId
            )
        }

        composable("promo_detail/{promoId}", // Rute ini yang akan dituju saat promo diklik
            arguments = listOf(
                navArgument("promoId") { type = NavType.StringType } // Asumsi ID promo adalah String
            )
        ) { backStackEntry ->
            val promoId = backStackEntry.arguments?.getString("promoId")
            promoId?.let {
                PromoDetailScreen(promoId = it, navController = navController) // Panggil PromoDetailScreen Anda
            }
        }

        // Wifi Dashboard - Untuk admin melihat daftar paket WiFi
        composable("wifi_dashboard") {
            val wifiViewModel: WifiViewModel = viewModel()
            WifiDashboardScreen(
                navController = navController,
                viewModel = wifiViewModel,
                onAddPackageClick = {
                    navController.navigate("wifi_management")
                },
                onEditPackageClick = { id ->
                    navController.navigate("wifi_management/$id")
                }
            )
        }

        composable("wifi_management") {
            val wifiViewModel: WifiViewModel = viewModel()
            WifiManagementScreen(
                navController = navController,
                viewModel = wifiViewModel,
                packageId = null
            )
        }

        composable("wifi_management/{packageId}") { backStackEntry ->
            val wifiViewModel: WifiViewModel = viewModel()
            val packageIdString = backStackEntry.arguments?.getString("packageId")
            val packageId = packageIdString?.toIntOrNull()
            WifiManagementScreen(
                navController = navController,
                viewModel = wifiViewModel,
                packageId = packageId
            )
        }

        composable("cctv_dashboard") {
            val viewModel: CctvViewModel = hiltViewModel()
            CctvDashboardScreen(
                navController = navController,
                viewModel = viewModel,
                onAddPackageClick = {
                    navController.navigate("cctv_management")
                },
                onEditPackageClick = { id ->
                    navController.navigate("cctv_management/$id")
                }
            )
        }


        composable("cctv_management") {
            val viewModel: CctvViewModel = hiltViewModel()
            CctvManagementScreen(
                navController = navController,
                viewModel = viewModel,
                packageId = null
            )
        }

        composable("cctv_management/{packageId}?") { backStackEntry ->
            val packageId = backStackEntry.arguments?.getString("packageId")?.toIntOrNull()
            CctvManagementScreen(navController, packageId)
        }
    }
}
