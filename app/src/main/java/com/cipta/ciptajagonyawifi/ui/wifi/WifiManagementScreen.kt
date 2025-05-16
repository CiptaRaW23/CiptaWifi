package com.cipta.ciptajagonyawifi.ui.wifi

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.cipta.ciptajagonyawifi.model.WifiPackage
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WifiManagementScreen(
    navController: NavController,
    packageId: Int? = null,
    viewModel: WifiViewModel = viewModel()
) {
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    var id by remember { mutableStateOf(0) }
    var name by remember { mutableStateOf("") }
    var speed by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var promo by remember { mutableStateOf("") }

    // Contoh generateId, kamu bisa ganti sesuai kebutuhan
    fun generateId(): Int = (0..10000).random()

    LaunchedEffect(packageId) {
        if (packageId != null) {
            val pkg = viewModel.wifiPackages.value.find { it.id == packageId }
            pkg?.let {
                id = it.id
                name = it.name
                speed = it.speed
                price = it.price
                description = it.description
                promo = it.promo
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = if (packageId != null) "Edit Paket Wifi" else "Tambah Paket Wifi")
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxWidth()
                .verticalScroll(scrollState)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nama Paket") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = speed,
                onValueChange = { speed = it },
                label = { Text("Kecepatan") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Harga") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Deskripsi") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = promo,
                onValueChange = { promo = it },
                label = { Text("Promo") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (packageId != null) {
                        viewModel.updateWifiPackage(
                            docId = id.toString(), // pastikan docId sesuai tipe Firestore (biasanya string)
                            wifiPackage = WifiPackage(id, name, speed, price, description, promo)
                        )
                    } else {
                        viewModel.addWifiPackage(
                            WifiPackage(generateId(), name, speed, price, description, promo)
                        )
                    }
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Simpan")
            }

            if (packageId != null) {
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = {
                        scope.launch {
                            viewModel.deleteWifiPackage(id.toString())
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Hapus")
                }
            }
        }
    }
}
