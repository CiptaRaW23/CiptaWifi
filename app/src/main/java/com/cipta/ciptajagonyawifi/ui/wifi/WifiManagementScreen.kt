package com.cipta.ciptajagonyawifi.ui.wifi

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.cipta.ciptajagonyawifi.model.WifiPackage
import kotlinx.coroutines.launch


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
    var docId by remember { mutableStateOf("") }
    val imageUrls = remember { mutableStateListOf<String>() }

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
                docId = it.docId
                imageUrls.clear()
                imageUrls.addAll(it.imageUrls)
                if (imageUrls.isEmpty()) {
                    imageUrls.add("")
                }
            }
        } else {
            id = generateId()
            name = ""
            speed = ""
            price = ""
            description = ""
            promo = ""
            docId = ""
            imageUrls.clear()
            imageUrls.add("")
        }
    }

    Scaffold(
        topBar = {
            androidx.compose.material.TopAppBar(
                title = {
                    Text(
                        text = if (packageId != null) "Edit Paket WiFi" else "Tambah Paket WiFi",
                        color = Color.White
                    )
                },
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

            Text(text = "URL Gambar (Satu per baris):", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                imageUrls.forEachIndexed { index, url ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = url,
                            onValueChange = { newValue -> imageUrls[index] = newValue },
                            label = { Text("URL Gambar ${index + 1}") },
                            modifier = Modifier.weight(1f)
                        )

                        if (imageUrls.size > 1) {
                            IconButton(onClick = { imageUrls.removeAt(index) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Hapus URL")
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Button(
                    onClick = { imageUrls.add("") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Tambah URL Gambar")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Tambah Gambar")
                }
            }

            Button(
                onClick = {
                    val wifiPackage = WifiPackage(id, name, speed, price, description, promo, docId)
                    if (packageId != null) {
                        viewModel.updateWifiPackage(wifiPackage)
                    } else {
                        viewModel.addWifiPackage(wifiPackage)
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
                            viewModel.deleteWifiPackage(docId)
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
