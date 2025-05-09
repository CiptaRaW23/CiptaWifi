package com.cipta.ciptajagonyawifi.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cipta.ciptajagonyawifi.ui.promo.PromoDashboardScreen
import com.cipta.ciptajagonyawifi.ui.promo.PromoViewModel
import com.cipta.ciptajagonyawifi.ui.admin.AdminDashboardScreen
import com.cipta.ciptajagonyawifi.ui.admin.LoginAdminScreen
import com.cipta.ciptajagonyawifi.ui.article.ArticleDetailScreen
import com.cipta.ciptajagonyawifi.ui.detail.DetailScreen
import com.cipta.ciptajagonyawifi.ui.form.FormScreen
import com.cipta.ciptajagonyawifi.ui.home.HomeScreen
import com.cipta.ciptajagonyawifi.ui.home.WifiScreen
import com.cipta.ciptajagonyawifi.ui.promo.PromoManagementScreen
import com.cipta.ciptajagonyawifi.ui.splash.SplashScreen


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

        composable("detail/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: 0
            DetailScreen(id, navController)
        }

        composable("form/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: 0
            FormScreen(packageId = id)
        }

        composable("article/{articleId}") { backStackEntry ->
            val articleId = backStackEntry.arguments?.getString("articleId")
            articleId?.let {
                ArticleDetailScreen(articleId = it, navController = navController) // Pass navController explicitly
            }
        }

        composable("login") {
            LoginAdminScreen(navController)
        }

        composable("admin_dashboard") {
            AdminDashboardScreen(navController)
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
    }
}
