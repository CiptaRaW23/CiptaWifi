package com.cipta.ciptajagonyawifi.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cipta.ciptajagonyawifi.ui.artikel.ArticleDetailScreen
import com.cipta.ciptajagonyawifi.ui.detail.DetailScreen
import com.cipta.ciptajagonyawifi.ui.form.FormScreen
import com.cipta.ciptajagonyawifi.ui.home.HomeScreen
import com.cipta.ciptajagonyawifi.ui.home.WifiScreen
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

        composable("article/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: 0
            ArticleDetailScreen(articleId = id, navController = navController)
        }

    }
}
