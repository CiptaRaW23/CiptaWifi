package com.cipta.ciptajagonyawifi.ui.cctv

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CctvDashboardScreen(
    navController: NavController,
    viewModel: CctvViewModel = viewModel(),
    onAddPackageClick: () -> Unit,
    onEditPackageClick: (Int) -> Unit
) {
    val packages by viewModel.cctvPackages.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Kelola Paket CCTV") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddPackageClick) {
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
