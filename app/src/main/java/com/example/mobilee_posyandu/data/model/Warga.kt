package com.example.mobilee_posyandu.data.model

data class Warga(
    val nik: String,
    val namaLengkap: String,
    val tanggalLahir: String,
    val jenisKelamin: String, // "Laki-laki" | "Perempuan"
    val namaAyah: String = "-",
    val namaIbu: String = "-",
    val status: KategoriWarga = KategoriWarga.BALITA,
    val alamat: String = "Desa Sejahtera RT 02 / RW 01"
)

