package com.example.mobilee_posyandu.ui.navigation

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Dashboard : Screen("dashboard")
    data object WargaList : Screen("warga_list")
    data object WargaDetail : Screen("warga_detail/{nik}") {
        fun createRoute(nik: String) = "warga_detail/$nik"
    }
    data object TambahWarga : Screen("tambah_warga")
    data object CatatPemeriksaan : Screen("catat_pemeriksaan/{nik}") {
        fun createRoute(nik: String) = "catat_pemeriksaan/$nik"
    }
    data object Jadwal : Screen("jadwal")
    data object Edukasi : Screen("edukasi")
    data object StatusCodeShowcase : Screen("status_code_showcase")
}

