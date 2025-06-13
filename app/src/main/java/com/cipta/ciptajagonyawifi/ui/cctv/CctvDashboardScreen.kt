package com.cipta.ciptajagonyawifi.ui.cctv

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController



@Composable
fun CctvDashboardScreen(
    navController: NavController,
    viewModel: CctvViewModel = viewModel(),
    onAddPackageClick: () -> Unit,
    onEditPackageClick: (Int) -> Unit
) {
    val packages by viewModel.cctvPackages.collectAsState()

    Scaffold(
        topBar = {
            androidx.compose.material.TopAppBar(
                title = { androidx.compose.material.Text("Kelola Paket CCTV", color = Color.White) },
                backgroundColor = Color(0xFF1B5E20),
                navigationIcon = {
                    androidx.compose.material.IconButton(onClick = { navController.popBackStack() }) {
                        androidx.compose.material.Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddPackageClick,
                containerColor = Color(0xFF1B5E20),
                contentColor = Color.White
            ) {
                Text("+")
            }
        }
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier.fillMaxSize()
        ) {
            items(packages) { cctvPackage ->
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .clickable { onEditPackageClick(cctvPackage.id) },
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = cctvPackage.name, style = MaterialTheme.typography.headlineSmall)
                        Text(text = "Resolusi: ${cctvPackage.resolution}")
                        Text(text = "Harga: ${cctvPackage.price}")
                        Text(text = cctvPackage.description.take(50) + "...")
                    }
                }
            }
        }
    }
}
