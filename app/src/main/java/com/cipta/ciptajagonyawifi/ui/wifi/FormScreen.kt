package com.cipta.ciptajagonyawifi.ui.wifi

import android.graphics.Paint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.cipta.ciptajagonyawifi.api.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(
    packageId: String?,
    type: String?,
    navController: NavController = rememberNavController(),
    viewModel: FormViewModel = hiltViewModel()
) {
    var step by remember { mutableStateOf(1) }
    var showConfirmDialog by remember { mutableStateOf(false) }
    var showSuccess by remember { mutableStateOf(false) }
    var isLoadingSubmit by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Formulir Pendaftaran",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Kembali",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = Color.Transparent,
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1B5E20),
                        Color(0xFFFFEE58)
                    )
                )
            )
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            StepIndicator(step)
            Spacer(Modifier.height(16.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                elevation = CardDefaults.cardElevation(8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = when (step) {
                            1 -> "Langkah 1: Data Pribadi"
                            2 -> "Langkah 2: Informasi Identitas"
                            3 -> "Langkah 3: Lokasi & Paket"
                            else -> ""
                        },
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF1B5E20),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    when (step) {
                        1 -> Step1DataPribadi(viewModel)
                        2 -> Step2Identitas(viewModel)
                        3 -> Step3LokasiPaket(packageId?.toIntOrNull(), viewModel)
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (step > 1) {
                    OutlinedButton(
                        onClick = {
                            showSuccess = false
                            step--
                        },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF1B5E20)),
                        border = ButtonDefaults.outlinedButtonBorder.copy(brush = SolidColor(Color(0xFF1B5E20)))
                    ) {
                        Text("Kembali")
                    }
                }
                Button(
                    onClick = {
                        if (step < 3) {
                            step++
                        } else {
                            showConfirmDialog = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B5E20)),
                    enabled = !isLoadingSubmit
                ) {
                    if (isLoadingSubmit) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(if (step < 3) "Lanjut" else "Kirim", color = Color.White)
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
        }
    }

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Konfirmasi Pengiriman") },
            text = { Text("Apakah data yang Anda masukkan sudah benar dan siap dikirim?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showConfirmDialog = false
                        isLoadingSubmit = true
                        val formData = viewModel.toFormData()

                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                val response = RetrofitInstance.api.sendForm(formData)
                                withContext(Dispatchers.Main) {
                                    isLoadingSubmit = false
                                    if (response.isSuccessful) {
                                        showSuccess = true
                                    } else {
                                        println("Gagal mengirim data: ${response.code()} ${response.message()}")
                                    }
                                }
                            } catch (e: Exception) {
                                withContext(Dispatchers.Main) {
                                    isLoadingSubmit = false
                                    println("Error mengirim data: ${e.message}")
                                }
                            }
                        }
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF1B5E20))
                ) {
                    Text("Ya, Kirim")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showConfirmDialog = false },
                    colors = ButtonDefaults.textButtonColors(contentColor = Color.Gray)
                ) {
                    Text("Tidak")
                }
            }
        )
    }

    if (showSuccess) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f)),
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.padding(32.dp)
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
                    Text(
                        "Data berhasil dikirim!",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Terima kasih atas pendaftaran Anda.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                    Spacer(Modifier.height(24.dp))
                    Button(
                        onClick = {
                            showSuccess = false
                            navController.navigate("home") {
                                popUpTo("home") { inclusive = true }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B5E20))
                    ) {
                        Text("Kembali ke Beranda", color = Color.White)
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
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        (1..3).forEach { i ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(if (i <= step) Color(0xFF1B5E20) else Color(0xFFBDBDBD)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("$i", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    text = when (i) {
                        1 -> "Data Pribadi"
                        2 -> "Identitas"
                        3 -> "Lokasi & Paket"
                        else -> ""
                    },
                    fontSize = 12.sp,
                    color = if (i <= step) Color(0xFF1B5E20) else Color.Gray,
                    fontWeight = FontWeight.Medium
                )
            }
            if (i < 3) {
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(2.dp)
                        .background(if (i < step) Color(0xFF1B5E20) else Color(0xFFE0E0E0))
                )
            }
        }
    }
}

@Composable
fun StyledOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Color.DarkGray) },
        modifier = modifier.fillMaxWidth(),
        readOnly = readOnly,
        trailingIcon = trailingIcon,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF1B5E20),
            unfocusedBorderColor = Color.Gray.copy(alpha = 0.6f),
            focusedLabelColor = Color(0xFF1B5E20),
            unfocusedLabelColor = Color.Gray.copy(alpha = 0.6f),
            cursorColor = Color(0xFF1B5E20)
        ),
        shape = RoundedCornerShape(8.dp)
    )
}

@Composable
fun Step1DataPribadi(viewModel: FormViewModel) {
    StyledOutlinedTextField(
        value = viewModel.nama,
        onValueChange = { viewModel.nama = it },
        label = "Nama Lengkap",
    )
    Spacer(Modifier.height(8.dp))
    StyledOutlinedTextField(
        value = viewModel.nomorHp,
        onValueChange = { viewModel.nomorHp = it },
        label = "Nomor HP",
    )
    Spacer(Modifier.height(8.dp))
    GenderDropdown(viewModel)
    Spacer(Modifier.height(8.dp))
    StyledOutlinedTextField(
        value = viewModel.tempatLahir,
        onValueChange = { viewModel.tempatLahir = it },
        label = "Tempat Lahir",
    )
    Spacer(Modifier.height(8.dp))
    StyledOutlinedTextField(
        value = viewModel.tanggalLahir,
        onValueChange = { viewModel.tanggalLahir = it },
        label = "Tanggal Lahir (DD/MM/YYYY)",
    )
}

@Composable
fun Step2Identitas(viewModel: FormViewModel) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
        viewModel.fotoIdentitasUri = uri?.toString()
    }

    Column {
        IdentitasDropdown(viewModel)
        Spacer(Modifier.height(8.dp))
        StyledOutlinedTextField(
            value = viewModel.nomorIdentitas,
            onValueChange = { viewModel.nomorIdentitas = it },
            label = "Nomor Identitas",
        )
        Spacer(Modifier.height(8.dp))
        StyledOutlinedTextField(
            value = viewModel.npwp,
            onValueChange = { viewModel.npwp = it },
            label = "NPWP (opsional)",
        )
        Spacer(Modifier.height(8.dp))
        StyledOutlinedTextField(
            value = viewModel.alamat,
            onValueChange = { viewModel.alamat = it },
            label = "Alamat Lengkap",
        )
        Spacer(Modifier.height(8.dp))

        Button(
            onClick = { launcher.launch("image/*") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
        ) {
            Text("Upload Foto Identitas", color = Color.White)
        }

        Spacer(Modifier.height(8.dp))

        selectedImageUri?.let { uri ->
            Image(
                painter = rememberAsyncImagePainter(uri),
                contentDescription = "Preview Foto Identitas",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun Step3LokasiPaket(packageId: Int?, viewModel: FormViewModel) {
    StyledOutlinedTextField(
        value = viewModel.linkMaps,
        onValueChange = { viewModel.linkMaps = it },
        label = "Link Lokasi Google Maps",
    )
    Spacer(Modifier.height(8.dp))
    JenisPelangganDropdown(viewModel)
    Spacer(Modifier.height(8.dp))
    PaketDropdown(packageId, viewModel)
    Spacer(Modifier.height(8.dp))
    SalesDropdown(viewModel)
    Spacer(Modifier.height(8.dp))
    StyledOutlinedTextField(
        value = viewModel.catatan,
        onValueChange = { viewModel.catatan = it },
        label = "Catatan (opsional)",
    )
    Spacer(Modifier.height(16.dp))
    Text(
        "Tanda Tangan:",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        color = Color.DarkGray,
        modifier = Modifier.padding(bottom = 8.dp)
    )
    SignaturePad(viewModel)
}

@Composable
fun GenderDropdown(viewModel: FormViewModel) {
    DropdownComponent(
        label = "Jenis Kelamin",
        options = listOf("Laki-laki", "Perempuan"),
        selectedOption = viewModel.jenisKelamin,
        onSelectedChange = { viewModel.jenisKelamin = it }
    )
}

@Composable
fun IdentitasDropdown(viewModel: FormViewModel) {
    DropdownComponent(
        label = "Jenis Identitas",
        options = listOf("KTP", "SIM", "Paspor"),
        selectedOption = viewModel.jenisIdentitas,
        onSelectedChange = { viewModel.jenisIdentitas = it }
    )
}

@Composable
fun JenisPelangganDropdown(viewModel: FormViewModel) {
    DropdownComponent(
        label = "Jenis Pelanggan",
        options = listOf("Pribadi", "Bisnis", "Instansi"),
        selectedOption = viewModel.jenisPelanggan,
        onSelectedChange = { viewModel.jenisPelanggan = it }
    )
}

@Composable
fun PaketDropdown(packageId: Int?, viewModel: FormViewModel) { // <-- Pastikan Int?
    val paketList = listOf("Paket 1 - 10 Mbps", "Paket 2 - 20 Mbps", "Paket 3 - 50 Mbps")
    val initialSelection = if (packageId != null && packageId >= 0 && packageId < paketList.size) {
        paketList[packageId]
    } else {
        paketList[0]
    }

    LaunchedEffect(initialSelection) {
        if (viewModel.paketPilihan.isEmpty()) {
            viewModel.paketPilihan = initialSelection
        }
    }

    DropdownComponent(
        label = "Paket Pilihan",
        options = paketList,
        selectedOption = viewModel.paketPilihan,
        onSelectedChange = { viewModel.paketPilihan = it }
    )
}

@Composable
fun SalesDropdown(viewModel: FormViewModel) {
    DropdownComponent(
        label = "Sales",
        options = listOf("Admin", "Sales 1", "Sales 2"),
        selectedOption = viewModel.sales,
        onSelectedChange = { viewModel.sales = it }
    )
}


@Composable
fun DropdownComponent(
    label: String,
    options: List<String>,
    selectedOption: String,
    onSelectedChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        StyledOutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            label = label,
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.DarkGray)
                }
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .background(Color.White)
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option, color = Color.DarkGray) },
                    onClick = {
                        onSelectedChange(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun SignaturePad(
    viewModel: FormViewModel,
    modifier: Modifier = Modifier,
    strokeWidth: Float = 4f
) {
    var paths by remember { mutableStateOf(mutableListOf<android.graphics.Path>()) }
    var currentPath by remember { mutableStateOf(android.graphics.Path()) }
    var lastPoint by remember { mutableStateOf(Offset(0f, 0f)) }

    DisposableEffect(Unit) {
        onDispose {
            // kosongkan saja, tidak ada kode di sini
        }
    }

    Column {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color.White, RoundedCornerShape(8.dp))
                .border(1.dp, Color.Gray.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            currentPath.moveTo(offset.x, offset.y)
                            lastPoint = offset
                        },
                        onDrag = { change, dragAmount ->
                            val newPointUnclamped = Offset(lastPoint.x + dragAmount.x, lastPoint.y + dragAmount.y)
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
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawIntoCanvas { canvas ->
                    val paint = Paint().apply {
                        color = android.graphics.Color.BLACK
                        this.strokeWidth = strokeWidth
                        style = android.graphics.Paint.Style.STROKE
                        isAntiAlias = true
                        strokeJoin = Paint.Join.ROUND
                        strokeCap = Paint.Cap.ROUND
                    }

                    for (path in paths) {
                        canvas.nativeCanvas.drawPath(path, paint)
                    }
                    canvas.nativeCanvas.drawPath(currentPath, paint)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                paths.clear()
                currentPath.reset()
                // TODO: reset signature di ViewModel juga
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(alpha = 0.8f)), // Warna merah sedikit transparan
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Hapus Tanda Tangan", color = Color.White)
        }
    }
}