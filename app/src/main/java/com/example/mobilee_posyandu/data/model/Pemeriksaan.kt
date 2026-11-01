package com.example.mobilee_posyandu.data.model

sealed interface PemeriksaanItem {
    val idPeriksa: Long
    val nik: String
    val tanggal: String
    val status: String
}

data class PemeriksaanBalita(
    override val idPeriksa: Long,
    override val nik: String,
    override val tanggal: String,
    val bb: Double, // kg
    val tb: Double, // cm
    val lk: Double, // Lingkar Kepala cm
    val la: Double, // Lingkar Lengan Atas cm
    val lp: Double, // Lingkar Perut cm
    override val status: String // "Gizi Baik (Normal)", "Risiko Stunting", "Gizi Kurang"
) : PemeriksaanItem

data class PemeriksaanBumil(
    override val idPeriksa: Long,
    override val nik: String,
    override val tanggal: String,
    val usiaKehamilan: String, // contoh: "Trimester 2 (24 Minggu)"
    val bb: Double,
    val tb: Double,
    val la: Double, // Lingkar Lengan Atas (skrining KEK)
    val lp: Double,
    val tensi: String, // contoh: "115/75"
    override val status: String // "Normal", "Risiko KEK", "Hipertensi Gestasional"
) : PemeriksaanItem

data class PemeriksaanRemaja(
    override val idPeriksa: Long,
    override val nik: String,
    override val tanggal: String,
    val bb: Double,
    val tb: Double,
    val tensi: String,
    val hemoglobin: Double, // g/dL
    val gula: Double, // mg/dL
    override val status: String // "Normal", "Anemia Ringan"
) : PemeriksaanItem

data class PemeriksaanLansia(
    override val idPeriksa: Long,
    override val nik: String,
    override val tanggal: String,
    val bb: Double,
    val tb: Double,
    val tensi: String,
    val gula: Double,
    val riwayatPenyakit: String,
    override val status: String // "Terkontrol", "Dirujuk ke Puskesmas"
) : PemeriksaanItem

