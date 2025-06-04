package com.cipta.ciptajagonyawifi.ui.cctv

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.cipta.ciptajagonyawifi.model.CctvPackage
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CctvManagementScreen(
    navController: NavController,
    packageId: Int? = null,
    viewModel: CctvViewModel = viewModel()
) {
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    var id by remember { mutableStateOf(0) }
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var resolution by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var features by remember { mutableStateOf("") }
    var docId by remember { mutableStateOf("") }

    fun generateId(): Int = (0..10000).random()

    LaunchedEffect(packageId) {
        if (packageId != null) {
            val pkg = viewModel.cctvPackages.value.find { it.id == packageId }
            pkg?.let {
                id = it.id
                name = it.name
                price = it.price
                resolution = it.resolution
                description = it.description
                features = it.features
                docId = it.docId
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = if (packageId != null) "Edit Paket CCTV" else "Tambah Paket CCTV")
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
                value = price,
                onValueChange = { price = it },
                label = { Text("Harga") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = resolution,
                onValueChange = { resolution = it },
                label = { Text("Resolusi CCTV") },
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
                value = features,
                onValueChange = { features = it },
                label = { Text("Fitur Tambahan") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val cctvPackage = CctvPackage(
                        id = if (packageId != null) id else generateId(),
                        name = name,
                        price = price,
                        resolution = resolution,
                        description = description,
                        features = features,
                        docId = docId
                    )
                    if (packageId != null) {
                        viewModel.updateCctvPackage(cctvPackage)
                    } else {
                        viewModel.addCctvPackage(cctvPackage)
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
                            viewModel.deleteCctvPackage(docId)
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
