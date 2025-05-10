package com.cipta.ciptajagonyawifi.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AdminDashboardScreen(
    navController: NavController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F0F0))
            .padding(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Dashboard",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            DashboardButton("Kelola Promo") {
                navController.navigate("promo_dashboard")
            }

            DashboardButton("Kelola Artikel") {
                navController.navigate("article_dashboard")
            }

            DashboardButton("Logout", isDanger = true) {
                FirebaseAuth.getInstance().signOut()
                navController.navigate("login") {
                    popUpTo("admin_dashboard") { inclusive = true }
                }
            }
        }
    }
}

@Composable
fun DashboardButton(
    text: String,
    isDanger: Boolean = false,
    onClick: () -> Unit
) {
    val backgroundColor = if (isDanger) Color.Red else Color(0xFF1976D2) // biru
    val textColor = if (isDanger) Color.White else Color.White

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .height(50.dp)
    ) {
        Text(text = text, color = textColor)
    }
}
