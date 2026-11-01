package com.example.mobilee_posyandu.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mobilee_posyandu.ui.features.auth.LoginScreen
import com.example.mobilee_posyandu.ui.features.dashboard.DashboardScreen
import com.example.mobilee_posyandu.ui.features.edukasi.EdukasiScreen
import com.example.mobilee_posyandu.ui.features.jadwal.JadwalScreen
import com.example.mobilee_posyandu.ui.features.pemeriksaan.CatatPemeriksaanScreen
import com.example.mobilee_posyandu.ui.features.status.StatusCodeShowcaseScreen
import com.example.mobilee_posyandu.ui.features.warga.TambahWargaScreen
import com.example.mobilee_posyandu.ui.features.warga.WargaDetailScreen
import com.example.mobilee_posyandu.ui.features.warga.WargaListScreen

@Composable
fun PosyanduNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Login.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onNavigateToWargaList = { navController.navigate(Screen.WargaList.route) },
                onNavigateToTambahWarga = { navController.navigate(Screen.TambahWarga.route) },
                onNavigateToJadwal = { navController.navigate(Screen.Jadwal.route) },
                onNavigateToEdukasi = { navController.navigate(Screen.Edukasi.route) },
                onNavigateToDetailWarga = { nik ->
                    navController.navigate(Screen.WargaDetail.createRoute(nik))
                },
                onNavigateToStatusCodes = {
                    navController.navigate(Screen.StatusCodeShowcase.route)
                },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Dashboard.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.WargaList.route) {
            WargaListScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToDetail = { nik ->
                    navController.navigate(Screen.WargaDetail.createRoute(nik))
                },
                onNavigateToTambah = { navController.navigate(Screen.TambahWarga.route) }
            )
        }

        composable(
            route = Screen.WargaDetail.route,
            arguments = listOf(navArgument("nik") { type = NavType.StringType })
        ) { backStackEntry ->
            val nik = backStackEntry.arguments?.getString("nik") ?: ""
            WargaDetailScreen(
                nik = nik,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToCatatPemeriksaan = { nikPeriksa ->
                    navController.navigate(Screen.CatatPemeriksaan.createRoute(nikPeriksa))
                }
            )
        }

        composable(Screen.TambahWarga.route) {
            TambahWargaScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.CatatPemeriksaan.route,
            arguments = listOf(navArgument("nik") { type = NavType.StringType })
        ) { backStackEntry ->
            val nik = backStackEntry.arguments?.getString("nik") ?: ""
            CatatPemeriksaanScreen(
                nik = nik,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Jadwal.route) {
            JadwalScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Edukasi.route) {
            EdukasiScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.StatusCodeShowcase.route) {
            StatusCodeShowcaseScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

