package com.cipta.ciptajagonyawifi.ui.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.cipta.ciptajagonyawifi.data.dummyPackages

@Composable
fun DetailScreen(packageId: Int, navController: NavController) {
    val pkg = dummyPackages.find { it.id == packageId } ?: return

    Column(modifier = Modifier.padding(16.dp)) {
        Text(pkg.name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(pkg.description, style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Harga: ${pkg.price}", style = MaterialTheme.typography.bodyLarge, color = Color.Green)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Promo: ${pkg.promo}", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("form/${pkg.id}") },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Beli Sekarang", color = Color.White)
        }
    }
}
