package com.example.mobilee_posyandu.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

enum class HttpStatusCategory(
    val title: String,
    val badgeColor: Color,
    val surfaceColor: Color
) {
    SUCCESS("2xx Sukses", Color(0xFF10B981), Color(0xFFECFDF5)),
    CLIENT_ERROR("4xx Kesalahan Klien", Color(0xFFF59E0B), Color(0xFFFFFBEB)),
    SERVER_ERROR("5xx Gangguan Server", Color(0xFFEF4444), Color(0xFFFEF2F2)),
    NETWORK_ERROR("Masalah Jaringan", Color(0xFF6366F1), Color(0xFFEEF2FF))
}

enum class HttpStatusCodeInfo(
    val code: String,
    val category: HttpStatusCategory,
    val title: String,
    val subtitle: String,
    val explanation: String,
    val suggestion: String,
    val icon: ImageVector,
    val primaryActionText: String,
    val secondaryActionText: String? = "Kembali ke Beranda"
) {
    // --- 2xx Sukses ---
    OK_200(
        code = "200",
        category = HttpStatusCategory.SUCCESS,
        title = "Permintaan Berhasil Diproses",
        subtitle = "OK — Data Berhasil Dimuat",
        explanation = "Permintaan data atau pembaruan rekam medis telah berhasil diproses oleh server e-Posyandu tanpa kendala.",
        suggestion = "Semua data sinkron dan siap digunakan.",
        icon = Icons.Default.CheckCircle,
        primaryActionText = "Lanjutkan Aktivitas"
    ),
    CREATED_201(
        code = "201",
        category = HttpStatusCategory.SUCCESS,
        title = "Data Baru Berhasil Dibuat",
        subtitle = "Created — Pendaftaran / Rekam Medis Tersimpan",
        explanation = "Entitas data baru (seperti biodata warga atau penimbangan balita) telah sukses tercatat ke dalam database pusat.",
        suggestion = "Data langsung terintegrasi dengan laporan bulanan posyandu.",
        icon = Icons.Default.TaskAlt,
        primaryActionText = "Lihat Data Tersimpan",
        secondaryActionText = "Tambah Data Baru"
    ),

    // --- 4xx Client Error ---
    BAD_REQUEST_400(
        code = "400",
        category = HttpStatusCategory.CLIENT_ERROR,
        title = "Format Data Tidak Sesuai",
        subtitle = "Bad Request — Parameter Tidak Valid",
        explanation = "Server tidak dapat memproses permintaan karena struktur data yang dikirimkan tidak sesuai dengan standar API.",
        suggestion = "Periksa kembali format isian angka, tanggal, atau parameter pencarian.",
        icon = Icons.Default.ErrorOutline,
        primaryActionText = "Periksa Isian Form"
    ),
    UNAUTHORIZED_401(
        code = "401",
        category = HttpStatusCategory.CLIENT_ERROR,
        title = "Sesi Anda Telah Berakhir",
        subtitle = "Unauthorized — Kredensial Tidak Sah",
        explanation = "Akses ditolak karena sesi login Anda telah kedaluwarsa atau token autentikasi tidak ditemukan.",
        suggestion = "Silakan login kembali dengan username dan kata sandi Anda.",
        icon = Icons.Default.LockPerson,
        primaryActionText = "Masuk Ulang",
        secondaryActionText = null
    ),
    FORBIDDEN_403(
        code = "403",
        category = HttpStatusCategory.CLIENT_ERROR,
        title = "Akses Menu Ditolak",
        subtitle = "Forbidden — Izin Tidak Cukup",
        explanation = "Akun Anda tidak memiliki hak akses untuk membuka modul ini. Fitur ini mungkin hanya dapat diakses oleh Bidan Desa atau Ketua Posyandu.",
        suggestion = "Hubungi Administrator Posyandu jika Anda memerlukan hak akses tambahan.",
        icon = Icons.Default.GppBad,
        primaryActionText = "Kembali ke Dashboard",
        secondaryActionText = "Hubungi Admin"
    ),
    NOT_FOUND_404(
        code = "404",
        category = HttpStatusCategory.CLIENT_ERROR,
        title = "Halaman Tidak Ditemukan",
        subtitle = "Not Found — Data Hilang / Pindah",
        explanation = "Halaman, tautan rute, atau data NIK warga yang Anda cari tidak tersedia di server atau tautan telah diubah.",
        suggestion = "Pastikan NIK atau URL rute yang Anda tuju sudah benar.",
        icon = Icons.Default.SearchOff,
        primaryActionText = "Kembali ke Beranda",
        secondaryActionText = "Cari Warga Lain"
    ),
    UNPROCESSABLE_422(
        code = "422",
        category = HttpStatusCategory.CLIENT_ERROR,
        title = "Validasi Data Gagal",
        subtitle = "Unprocessable Entity — Input Belum Lengkap",
        explanation = "Terdapat isian formulir yang belum memenuhi validasi (contoh: NIK kurang dari 16 digit, atau tinggi badan di luar batas normal).",
        suggestion = "Koreksi baris formulir yang ditandai merah lalu kirim ulang.",
        icon = Icons.Default.AssignmentLate,
        primaryActionText = "Perbaiki Formulir"
    ),

    // --- 5xx Server Error ---
    INTERNAL_SERVER_ERROR_500(
        code = "500",
        category = HttpStatusCategory.SERVER_ERROR,
        title = "Gangguan Internal Server",
        subtitle = "Internal Server Error — Masalah Sistem",
        explanation = "Terjadi kendala teknis pada server Laravel atau layanan database MySQL sedang mengalami beban tinggi.",
        suggestion = "Tim teknis sedang menangani kendala ini. Silakan coba kembali dalam beberapa saat.",
        icon = Icons.Default.Dns,
        primaryActionText = "Muat Ulang Halaman",
        secondaryActionText = "Laporkan Masalah"
    ),
    BAD_GATEWAY_502(
        code = "502",
        category = HttpStatusCategory.SERVER_ERROR,
        title = "Gerbang Server Tidak Merespon",
        subtitle = "Bad Gateway — Koneksi Hulu Terputus",
        explanation = "Server web menerima respons yang tidak valid dari server backend saat mencoba memproses permintaan Anda.",
        suggestion = "Periksa status server aplikasi atau refresh beberapa saat lagi.",
        icon = Icons.Default.CloudOff,
        primaryActionText = "Coba Hubungkan Ulang"
    ),
    SERVICE_UNAVAILABLE_503(
        code = "503",
        category = HttpStatusCategory.SERVER_ERROR,
        title = "Layanan Sedang Pemeliharaan",
        subtitle = "Service Unavailable — Maintenance Mode",
        explanation = "Sistem e-Posyandu sedang menjalani pemeliharaan sistem rutin atau peningkatan kapasitas server.",
        suggestion = "Sistem akan segera aktif kembali setelah pemeliharaan selesai.",
        icon = Icons.Default.Engineering,
        primaryActionText = "Cek Status Sistem",
        secondaryActionText = "Tutup Aplikasi"
    ),

    // --- Network / Offline ---
    NETWORK_OFFLINE(
        code = "OFFLINE",
        category = HttpStatusCategory.NETWORK_ERROR,
        title = "Tidak Ada Koneksi Internet",
        subtitle = "Connection Lost — Server Tidak Terjangkau",
        explanation = "Perangkat Anda sedang tidak terhubung ke jaringan internet, atau server Laravel lokal (php artisan serve) belum aktif.",
        suggestion = "Pastikan Wi-Fi/Data seluler aktif atau jalankan server lokal pada port 8000.",
        icon = Icons.Default.WifiOff,
        primaryActionText = "Coba Sambungkan",
        secondaryActionText = "Gunakan Mode Offline"
    );
}

