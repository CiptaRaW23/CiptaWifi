package com.cipta.ciptajagonyawifi.ui.form

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.cipta.ciptajagonyawifi.R
import com.cipta.ciptajagonyawifi.model.FormData
import com.cipta.ciptajagonyawifi.api.RetrofitInstance
import com.cipta.ciptajagonyawifi.data.dummyPackages
import com.google.android.gms.location.LocationServices
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

@Composable
fun FormScreen(packageId: Int) {
    val pkg = dummyPackages.find { it.id == packageId } ?: return

    var name by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val isFormValid = name.isNotBlank() && address.isNotBlank() && phone.length >= 10
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            getCurrentAddress(context) {
                address = it
            }
        } else {
            Toast.makeText(context, "Izin lokasi ditolak", Toast.LENGTH_SHORT).show()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF006400), Color(0xFF90EE90))
                )
            )
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(24.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_wifi),
                contentDescription = "WiFi Icon",
                modifier = Modifier.size(64.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Daftar untuk ${pkg.name}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            IconTextField(value = name, onValueChange = { name = it }, label = "Nama", icon = Icons.Default.Person)
            Spacer(modifier = Modifier.height(8.dp))

            IconTextField(value = address, onValueChange = { address = it }, label = "Alamat", icon = Icons.Default.LocationOn)

            TextButton(
                onClick = {
                    if (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    } else {
                        getCurrentAddress(context) {
                            address = it
                        }
                    }
                }
            ) {
                Text("📍 Gunakan Lokasi Saya", color = Color(0xFF006400))
            }

            Spacer(modifier = Modifier.height(8.dp))

            IconTextField(value = phone, onValueChange = { phone = it }, label = "No WA", icon = Icons.Default.Phone)
            Spacer(modifier = Modifier.height(24.dp))

            if (isLoading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = {
                        isLoading = true
                        val data = FormData(name, address, phone, pkg.name)

                        RetrofitInstance.api.submitForm(data).enqueue(object :
                            Callback<ResponseBody> {
                            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                                isLoading = false
                                if (response.isSuccessful) {
                                    Toast.makeText(context, "Pendaftaran berhasil!", Toast.LENGTH_SHORT).show()
                                    name = ""
                                    address = ""
                                    phone = ""
                                } else {
                                    Toast.makeText(context, "Gagal: ${response.code()}", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                isLoading = false
                                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                    ,
                    enabled = isFormValid,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A4A4A)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = "Kirim", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun IconTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { Icon(imageVector = icon, contentDescription = label) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        singleLine = true
    )
}

fun getCurrentAddress(context: Context, onAddressFound: (String) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED
    ) {
        Toast.makeText(context, "Izin lokasi belum diberikan", Toast.LENGTH_SHORT).show()
        return
    }

    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
        if (location != null) {
            Toast.makeText(context, "Lokasi ditemukan: ${location.latitude}, ${location.longitude}", Toast.LENGTH_SHORT).show()

            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            val address = addresses?.firstOrNull()?.getAddressLine(0) ?: "Alamat tidak ditemukan"
            onAddressFound(address)
        } else {
            Toast.makeText(context, "Tidak bisa mendapatkan lokasi", Toast.LENGTH_SHORT).show()
        }
    }.addOnFailureListener {
        Toast.makeText(context, "Gagal mendapatkan lokasi: ${it.message}", Toast.LENGTH_SHORT).show()
    }
}
