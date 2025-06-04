package com.cipta.ciptajagonyawifi.ui.wifi

import androidx.lifecycle.ViewModel
import com.cipta.ciptajagonyawifi.model.FormData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FormViewModel @Inject constructor() : ViewModel() {

    // Step 1
    var nama: String = ""
    var nomorHp: String = ""
    var tempatLahir: String = ""
    var tanggalLahir: String = ""
    var jenisKelamin: String = ""

    // Step 2
    var jenisIdentitas: String = ""
    var nomorIdentitas: String = ""
    var npwp: String = ""
    var alamat: String = ""
    var fotoIdentitasUri: String? = null  // URI disimpan sebagai string jika perlu dikirim

    // Step 3
    var linkMaps: String = ""
    var jenisPelanggan: String = ""
    var paketPilihan: String = ""
    var sales: String = ""
    var catatan: String = ""

    // Fungsi untuk mengonversi ke model FormData
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
