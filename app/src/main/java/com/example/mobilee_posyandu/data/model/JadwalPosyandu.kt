package com.example.mobilee_posyandu.data.model

data class JadwalPosyandu(
    val idJadwal: Long,
    val tanggalPelaksanaan: String,
    val kegiatanPelaksanaan: String,
    val statusPelaksanaan: String = "belum", // "belum", "selesai", "dibatalkan"
    val lokasi: String = "Balai Posyandu Anggrek"
)

data class InformasiUmum(
    val idInformasi: Long,
    val judul: String,
    val isiInformasi: String,
    val tanggalPost: String,
    val kategoriInfo: String // "Nutrisi", "Tumbuh Kembang", "Imunisasi", "Ibu Hamil", "Lansia"
)

