package com.example.mobilee_posyandu.ui.features.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobilee_posyandu.data.model.KategoriWarga
import com.example.mobilee_posyandu.data.repository.PosyanduRepository
import com.example.mobilee_posyandu.ui.components.CategoryBadge
import com.example.mobilee_posyandu.ui.components.PosyanduTopBar
import com.example.mobilee_posyandu.ui.components.StatCard
import com.example.mobilee_posyandu.ui.theme.*

import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun DashboardScreen(
    onNavigateToWargaList: () -> Unit,
    onNavigateToTambahWarga: () -> Unit,
    onNavigateToJadwal: () -> Unit,
    onNavigateToEdukasi: () -> Unit,
    onNavigateToDetailWarga: (String) -> Unit,
    onNavigateToStatusCodes: () -> Unit = {},
    onLogout: () -> Unit,
    viewModel: DashboardViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            PosyanduTopBar(
                title = "e-Posyandu",
                actions = {
                    IconButton(onClick = { viewModel.refresh() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Sinkronisasi Data API",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = {
                        viewModel.logout(onLogout)
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Keluar",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8FAFC))
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(4.dp))

                // Welcome Header Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = PosyanduPrimary)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(18.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .background(Color.White.copy(alpha = 0.2f), shape = CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.HealthAndSafety,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(30.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Selamat Datang 👋",
                                    color = Color.White.copy(alpha = 0.85f),
                                    fontSize = 13.sp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    color = if (uiState.isOnline) Color(0xFF10B981) else Color(0xFFF59E0B),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = if (uiState.isOnline) "● API Aktif" else "○ Mode Lokal",
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Text(
                                text = uiState.currentUser,
                                color = Color.White,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Quick Actions Row
            item {
                Text(
                    text = "Aksi Cepat",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickActionButton(
                        title = "Tambah Warga",
                        icon = Icons.Default.PersonAdd,
                        color = PosyanduPrimary,
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToTambahWarga
                    )
                    QuickActionButton(
                        title = "Daftar Warga",
                        icon = Icons.Default.People,
                        color = PosyanduSecondary,
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToWargaList
                    )
                    QuickActionButton(
                        title = "Jadwal",
                        icon = Icons.Default.Event,
                        color = Color(0xFFF59E0B),
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToJadwal
                    )
                    QuickActionButton(
                        title = "Edukasi",
                        icon = Icons.Default.MenuBook,
                        color = Color(0xFF8B5CF6),
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToEdukasi
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Error Code Showcase Banner Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToStatusCodes() },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBFDBFE))
                ) {
                    Row(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(Color(0xFF3B82F6), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Palette,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "🎨 Desain Status & Error Code",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E40AF)
                            )
                            Text(
                                text = "Lihat tampilan halaman 200, 404, 500, 401, dll.",
                                fontSize = 11.sp,
                                color = Color(0xFF3B82F6)
                            )
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = Color(0xFF3B82F6),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // Statistics Grid (2x2)
            item {
                Text(
                    text = "Statistik Sasaran Pelayanan",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        title = "Balita",
                        count = uiState.countBalita,
                        icon = Icons.Default.ChildCare,
                        accentColor = BalitaColor,
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToWargaList
                    )
                    StatCard(
                        title = "Ibu Hamil",
                        count = uiState.countBumil,
                        icon = Icons.Default.PregnantWoman,
                        accentColor = BumilColor,
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToWargaList
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        title = "Remaja",
                        count = uiState.countRemaja,
                        icon = Icons.Default.Groups,
                        accentColor = RemajaColor,
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToWargaList
                    )
                    StatCard(
                        title = "Lansia",
                        count = uiState.countLansia,
                        icon = Icons.Default.Elderly,
                        accentColor = LansiaColor,
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToWargaList
                    )
                }
            }

            // Upcoming Schedule Card
            item {
                val nextJadwal = uiState.nextJadwal
                if (nextJadwal != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Jadwal Terdekat",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Lihat Semua",
                            fontSize = 13.sp,
                            color = PosyanduPrimary,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.clickable { onNavigateToJadwal() }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateToJadwal() },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(14.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .background(
                                        color = Color(0xFFFEF3C7),
                                        shape = RoundedCornerShape(10.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.EventNote,
                                    contentDescription = null,
                                    tint = Color(0xFFD97706),
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = nextJadwal.tanggalPelaksanaan,
                                    fontSize = 12.sp,
                                    color = Color(0xFFD97706),
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = nextJadwal.kegiatanPelaksanaan,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "📍 ${nextJadwal.lokasi}",
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            }
                        }
                    }
                }
            }

            // Recent Warga List (Latest 3)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Warga Terbaru",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Semua (${uiState.countBalita + uiState.countBumil + uiState.countRemaja + uiState.countLansia})",
                        fontSize = 13.sp,
                        color = PosyanduPrimary,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable { onNavigateToWargaList() }
                    )
                }
            }

            items(uiState.recentWarga) { warga ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToDetailWarga(warga.nik) },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = warga.namaLengkap,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                color = TextPrimary
                            )
                            Text(
                                text = "NIK: ${warga.nik}",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }

                        CategoryBadge(kategori = warga.status)

                        Spacer(modifier = Modifier.width(6.dp))

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Detail",
                            tint = TextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun QuickActionButton(
    title: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 4.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(color.copy(alpha = 0.12f), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = TextPrimary,
                maxLines = 1
            )
        }
    }
}

