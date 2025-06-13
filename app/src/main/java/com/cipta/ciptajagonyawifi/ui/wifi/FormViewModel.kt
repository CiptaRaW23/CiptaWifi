package com.cipta.ciptajagonyawifi.ui.wifi

import androidx.lifecycle.ViewModel
import com.cipta.ciptajagonyawifi.model.FormData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

@HiltViewModel
class FormViewModel @Inject constructor() : ViewModel() {

    // Step 1
    var nama: String by mutableStateOf("")
    var nomorHp: String by mutableStateOf("")
    var tempatLahir: String by mutableStateOf("")
    var tanggalLahir: String by mutableStateOf("")
    var jenisKelamin: String by mutableStateOf("Laki-laki")

    // Step 2
    var jenisIdentitas: String by mutableStateOf("KTP")
    var nomorIdentitas: String by mutableStateOf("")
    var npwp: String by mutableStateOf("")
    var alamat: String by mutableStateOf("")
    var fotoIdentitasUri: String? by mutableStateOf(null)

    // Step 3
    var linkMaps: String by mutableStateOf("")
    var jenisPelanggan: String by mutableStateOf("Pribadi")
    var paketPilihan: String by mutableStateOf("")
    var sales: String by mutableStateOf("Admin")
    var catatan: String by mutableStateOf("")


    fun toFormData(): FormData {
        return FormData(
            nama = nama,
            nomorHp = nomorHp,
            gender = jenisKelamin,
            tempatLahir = tempatLahir,
            tanggalLahir = tanggalLahir,

            jenisIdentitas = jenisIdentitas,
            nomorIdentitas = nomorIdentitas,
            npwp = npwp,
            alamat = alamat,

            linkMaps = linkMaps,
            jenisPelanggan = jenisPelanggan,
            paket = paketPilihan,
            sales = sales,
            catatan = catatan
        )
    }

}