package com.cipta.ciptajagonyawifi.ui.wifi

import android.graphics.Paint
import android.graphics.Path
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter

@Composable
fun FormScreen(packageId: Int, navController: NavController = rememberNavController()) {
    var step by remember { mutableStateOf(1) }

    // State untuk tampilkan dialog konfirmasi
    var showConfirmDialog by remember { mutableStateOf(false) }

    // State untuk tampilkan sukses centang
    var showSuccess by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        StepIndicator(step)

        Spacer(Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(8.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                when (step) {
                    1 -> Step1DataPribadi()
                    2 -> Step2Identitas()
                    3 -> Step3LokasiPaket(packageId)
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (step > 1) {
                OutlinedButton(onClick = {
                    showSuccess = false  // reset jika mau balik step
                    step--
                }) {
                    Text("Kembali")
                }
            }
            Button(onClick = {
                if (step < 3) {
                    step++
                } else {
                    // Tampilkan dialog konfirmasi di step terakhir
                    showConfirmDialog = true
                }
            }) {
                Text(if (step < 3) "Lanjut" else "Kirim")
            }
        }
    }

    // Dialog konfirmasi
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Konfirmasi") },
            text = { Text("Apakah data sudah benar?") },
            confirmButton = {
                TextButton(onClick = {
                    showConfirmDialog = false
                    showSuccess = true
                }) {
                    Text("Ya")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text("Tidak")
                }
            }
        )
    }

    // Tampilkan centang sukses
    if (showSuccess) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xAA000000)),
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Sukses",
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(80.dp)
                    )
                    Spacer(Modifier.height(16.dp))
                    Text("Data berhasil dikirim!", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = {
                        showSuccess = false
                        navController.navigate("home")  // langsung kembali ke home, jangan ke "success"
                    }) {
                        Text("OK")
                    }
                }
            }
        }
    }
}


@Composable
fun StepIndicator(step: Int) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        (1..3).forEach { i ->
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(if (i <= step) Color(0xFF4B169D) else Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Text("$i", color = Color.White)
            }
        }
    }
}

@Composable
fun Step1DataPribadi() {
    var nama by remember { mutableStateOf("") }
    var nomorHp by remember { mutableStateOf("") }
    var tempatLahir by remember { mutableStateOf("") }
    var tanggalLahir by remember { mutableStateOf("") }

    OutlinedTextField(value = nama, onValueChange = { nama = it }, label = { Text("Nama Lengkap") }, modifier = Modifier.fillMaxWidth())
    Spacer(Modifier.height(8.dp))
    OutlinedTextField(value = nomorHp, onValueChange = { nomorHp = it }, label = { Text("Nomor HP") }, modifier = Modifier.fillMaxWidth())
    Spacer(Modifier.height(8.dp))
    GenderDropdown()
    Spacer(Modifier.height(8.dp))
    OutlinedTextField(value = tempatLahir, onValueChange = { tempatLahir = it }, label = { Text("Tempat Lahir") }, modifier = Modifier.fillMaxWidth())
    Spacer(Modifier.height(8.dp))
    OutlinedTextField(value = tanggalLahir, onValueChange = { tanggalLahir = it }, label = { Text("Tanggal Lahir") }, modifier = Modifier.fillMaxWidth())
}

@Composable
fun Step2Identitas() {
    var nomorIdentitas by remember { mutableStateOf("") }
    var npwp by remember { mutableStateOf("") }
    var alamat by remember { mutableStateOf("") }

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // Launcher untuk ambil gambar dari galeri
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    Column {
        IdentitasDropdown()
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = nomorIdentitas,
            onValueChange = { nomorIdentitas = it },
            label = { Text("Nomor Identitas") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = npwp,
            onValueChange = { npwp = it },
            label = { Text("NPWP (opsional)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = alamat,
            onValueChange = { alamat = it },
            label = { Text("Alamat Lengkap") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        Button(onClick = {
            launcher.launch("image/*")
        }) {
            Text("Upload Foto Identitas")
        }

        Spacer(Modifier.height(8.dp))

        selectedImageUri?.let { uri ->
            Image(
                painter = rememberAsyncImagePainter(uri),
                contentDescription = "Preview Foto",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        }
    }
}

@Composable
fun Step3LokasiPaket(packageId: Int) {
    var linkGoogleMaps by remember { mutableStateOf("") }
    var catatan by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
    ) {
        OutlinedTextField(
            value = linkGoogleMaps,
            onValueChange = { linkGoogleMaps = it },
            label = { Text("Link Lokasi Google Maps") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        JenisPelangganDropdown()
        Spacer(Modifier.height(8.dp))
        PaketDropdown(packageId)
        Spacer(Modifier.height(8.dp))
        SalesDropdown()
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = catatan,
            onValueChange = { catatan = it },
            label = { Text("Catatan (opsional)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))
        Text("Tanda Tangan:", style = MaterialTheme.typography.labelLarge)
        SignaturePad()
    }
}



@Composable
fun GenderDropdown() {
    DropdownComponent(label = "Jenis Kelamin", options = listOf("Laki-laki", "Perempuan"))
}

@Composable
fun IdentitasDropdown() {
    DropdownComponent(label = "Jenis Identitas", options = listOf("KTP", "SIM", "Paspor"))
}

@Composable
fun JenisPelangganDropdown() {
    DropdownComponent(label = "Jenis Pelanggan", options = listOf("Pribadi", "Bisnis", "Instansi"))
}

@Composable
fun PaketDropdown(packageId: Int) {
    val paketList = listOf("Paket 1 - 10 Mbps", "Paket 2 - 20 Mbps", "Paket 3 - 50 Mbps")
    DropdownComponent(label = "Paket Pilihan", options = paketList, defaultValue = paketList.getOrNull(packageId) ?: paketList[0])
}

@Composable
fun SalesDropdown() {
    DropdownComponent(label = "Sales", options = listOf("Admin", "Sales 1", "Sales 2"))
}

@Composable
fun DropdownComponent(label: String, options: List<String>, defaultValue: String = options[0]) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(defaultValue) }

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        selectedOption = option
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun SignaturePad(
    modifier: Modifier = Modifier,
    strokeWidth: Float = 4f
) {
    var paths by remember { mutableStateOf(mutableListOf<android.graphics.Path>()) }
    var currentPath by remember { mutableStateOf(android.graphics.Path()) }
    var lastPoint by remember { mutableStateOf(Offset(0f, 0f)) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(Color.White)
            .border(1.dp, Color.Gray)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        currentPath.moveTo(offset.x, offset.y)
                        lastPoint = offset
                    },
                    onDrag = { change, dragAmount ->
                        val newPointUnclamped = Offset(lastPoint.x + dragAmount.x, lastPoint.y + dragAmount.y)
                        // Batasi koordinat supaya tidak keluar area canvas
                        val newPoint = Offset(
                            newPointUnclamped.x.coerceIn(0f, size.width.toFloat()),
                            newPointUnclamped.y.coerceIn(0f, size.height.toFloat())
                        )
                        currentPath.lineTo(newPoint.x, newPoint.y)
                        lastPoint = newPoint
                    },
                    onDragEnd = {
                        paths.add(currentPath)
                        currentPath = android.graphics.Path()
                    }
                )
            }
            .zIndex(1f) // Penting untuk mencegah gesture tembus
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawIntoCanvas { canvas ->
                val paint = Paint().apply {
                    color = android.graphics.Color.BLACK
                    this.strokeWidth = strokeWidth
                    style = android.graphics.Paint.Style.STROKE
                    isAntiAlias = true
                }

                for (path in paths) {
                    canvas.nativeCanvas.drawPath(path, paint)
                }

                canvas.nativeCanvas.drawPath(currentPath, paint)
            }
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    Button(
        onClick = {
            paths.clear()
            currentPath.reset()
        },
        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
    ) {
        Text("Hapus Tanda Tangan", color = Color.White)
    }
}

