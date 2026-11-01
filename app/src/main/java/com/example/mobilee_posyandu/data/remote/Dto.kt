package com.example.mobilee_posyandu.data.remote

import com.google.gson.annotations.SerializedName

// --- Auth DTOs ---
data class LoginRequest(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String
)

data class LoginResponseData(
    @SerializedName("token") val token: String?,
    @SerializedName("role") val role: String?,
    @SerializedName("user") val user: UserDto? = null,
    @SerializedName("kader") val kader: KaderDto? = null,
    @SerializedName("warga") val warga: WargaDto? = null
)

data class UserDto(
    @SerializedName("id") val id: Long,
    @SerializedName("nama") val nama: String,
    @SerializedName("username") val username: String,
    @SerializedName("jabatan") val jabatan: String?
)

data class KaderDto(
    @SerializedName("id") val id: Long,
    @SerializedName("nama_kader") val namaKader: String,
    @SerializedName("username") val username: String
)

// --- Dashboard DTOs ---
data class DashboardResponseData(
    @SerializedName("stats") val stats: DashboardStatsDto,
    @SerializedName("jadwal_mendatang") val jadwalMendatang: List<JadwalDto> = emptyList(),
    @SerializedName("stok_kritis") val stokKritis: Int = 0,
    @SerializedName("warga_terbaru") val wargaTerbaru: List<WargaDto> = emptyList()
)

data class DashboardStatsDto(
    @SerializedName("total_warga") val totalWarga: Int = 0,
    @SerializedName("balita") val balita: Int = 0,
    @SerializedName("ibu_hamil") val ibuHamil: Int = 0,
    @SerializedName("remaja") val remaja: Int = 0,
    @SerializedName("lansia") val lansia: Int = 0
)

// --- Warga DTOs ---
data class WargaDto(
    @SerializedName("NIK") val nik: String,
    @SerializedName("nama_lengkap") val namaLengkap: String,
    @SerializedName("tanggal_lahir") val tanggalLahir: String,
    @SerializedName("jenis_kelamin") val jenisKelamin: String,
    @SerializedName("nama_ayah") val namaAyah: String? = "-",
    @SerializedName("nama_ibu") val namaIbu: String? = "-",
    @SerializedName("status") val status: String? = "Balita"
)

data class WargaStoreRequest(
    @SerializedName("NIK") val nik: String,
    @SerializedName("nama_lengkap") val namaLengkap: String,
    @SerializedName("tanggal_lahir") val tanggalLahir: String,
    @SerializedName("jenis_kelamin") val jenisKelamin: String,
    @SerializedName("nama_ayah") val namaAyah: String? = null,
    @SerializedName("nama_ibu") val namaIbu: String? = null,
    @SerializedName("status") val status: String
)

// --- Pemeriksaan DTOs ---
data class PemeriksaanBalitaRequest(
    @SerializedName("tanggal") val tanggal: String,
    @SerializedName("bb") val bb: Double,
    @SerializedName("tb") val tb: Double,
    @SerializedName("lk") val lk: Double? = null,
    @SerializedName("la") val la: Double? = null,
    @SerializedName("lp") val lp: Double? = null,
    @SerializedName("status") val status: String? = null
)

data class PemeriksaanBalitaDto(
    @SerializedName("id_periksa") val idPeriksa: Long,
    @SerializedName("tanggal") val tanggal: String,
    @SerializedName("bb") val bb: Double,
    @SerializedName("tb") val tb: Double,
    @SerializedName("lk") val lk: Double? = null,
    @SerializedName("la") val la: Double? = null,
    @SerializedName("lp") val lp: Double? = null,
    @SerializedName("status") val status: String
)

data class PemeriksaanBumilRequest(
    @SerializedName("tanggal") val tanggal: String,
    @SerializedName("usia_kehamilan") val usiaKehamilan: String,
    @SerializedName("bb") val bb: Double,
    @SerializedName("tb") val tb: Double? = null,
    @SerializedName("la") val la: Double? = null,
    @SerializedName("tensi") val tensi: String? = null,
    @SerializedName("status") val status: String? = null
)

data class PemeriksaanBumilDto(
    @SerializedName("id_periksa") val idPeriksa: Long,
    @SerializedName("tanggal") val tanggal: String,
    @SerializedName("usia_kehamilan") val usiaKehamilan: String,
    @SerializedName("bb") val bb: Double,
    @SerializedName("tb") val tb: Double? = null,
    @SerializedName("la") val la: Double? = null,
    @SerializedName("tensi") val tensi: String? = null,
    @SerializedName("status") val status: String
)

data class PemeriksaanRemajaRequest(
    @SerializedName("tanggal") val tanggal: String,
    @SerializedName("bb") val bb: Double,
    @SerializedName("tb") val tb: Double? = null,
    @SerializedName("tensi") val tensi: String? = null,
    @SerializedName("hemoglobin") val hemoglobin: Double? = null,
    @SerializedName("gula") val gula: Double? = null,
    @SerializedName("status") val status: String? = null
)

data class PemeriksaanRemajaDto(
    @SerializedName("id_periksa") val idPeriksa: Long,
    @SerializedName("tanggal") val tanggal: String,
    @SerializedName("bb") val bb: Double,
    @SerializedName("tb") val tb: Double? = null,
    @SerializedName("tensi") val tensi: String? = null,
    @SerializedName("hemoglobin") val hemoglobin: Double? = null,
    @SerializedName("gula") val gula: Double? = null,
    @SerializedName("status") val status: String
)

data class PemeriksaanLansiaRequest(
    @SerializedName("tanggal") val tanggal: String,
    @SerializedName("bb") val bb: Double,
    @SerializedName("tb") val tb: Double? = null,
    @SerializedName("tensi") val tensi: String? = null,
    @SerializedName("gula") val gula: Double? = null,
    @SerializedName("riwayat_penyakit") val riwayatPenyakit: String? = null,
    @SerializedName("status") val status: String? = null
)

data class PemeriksaanLansiaDto(
    @SerializedName("id_periksa") val idPeriksa: Long,
    @SerializedName("tanggal") val tanggal: String,
    @SerializedName("bb") val bb: Double,
    @SerializedName("tb") val tb: Double? = null,
    @SerializedName("tensi") val tensi: String? = null,
    @SerializedName("gula") val gula: Double? = null,
    @SerializedName("riwayat_penyakit") val riwayatPenyakit: String? = null,
    @SerializedName("status") val status: String
)

// --- Jadwal DTO ---
data class JadwalDto(
    @SerializedName("id_jadwal") val idJadwal: Long,
    @SerializedName("tanggal_pelaksanaan") val tanggalPelaksanaan: String,
    @SerializedName("kegiatan_pelaksanaan") val kegiatanPelaksanaan: String,
    @SerializedName("status_pelaksanaan") val statusPelaksanaan: String = "belum"
)

// --- Edukasi / Artikel DTO ---
data class ArtikelDto(
    @SerializedName("id_informasi") val idInformasi: Long,
    @SerializedName("judul") val judul: String,
    @SerializedName("isi_informasi") val isiInformasi: String,
    @SerializedName("tanggal_post") val tanggalPost: String,
    @SerializedName("kategori_info") val kategoriInfo: String
)

