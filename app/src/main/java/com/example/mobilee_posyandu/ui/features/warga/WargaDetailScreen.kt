package com.example.mobilee_posyandu.ui.features.warga

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddModerator
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobilee_posyandu.data.model.*
import com.example.mobilee_posyandu.data.repository.PosyanduRepository
import com.example.mobilee_posyandu.ui.components.CategoryBadge
import com.example.mobilee_posyandu.ui.components.PosyanduTopBar
import com.example.mobilee_posyandu.ui.theme.*

import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobilee_posyandu.ui.components.HttpStatusCodeInfo
import com.example.mobilee_posyandu.ui.components.HttpStatusLandingView

@Composable
fun WargaDetailScreen(
    nik: String,
    onNavigateBack: () -> Unit,
    onNavigateToCatatPemeriksaan: (String) -> Unit,
    viewModel: WargaViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val warga = uiState.wargaList.find { it.nik == nik }

    if (warga == null) {
        Scaffold(
            topBar = {
                PosyanduTopBar(
                    title = "Data Tidak Ditemukan",
                    canNavigateBack = true,
                    onNavigateBack = onNavigateBack
                )
            }
        ) { innerPadding ->
            HttpStatusLandingView(
                statusInfo = HttpStatusCodeInfo.NOT_FOUND_404,
                onPrimaryAction = onNavigateBack,
                onSecondaryAction = onNavigateBack,
                modifier = Modifier.padding(innerPadding)
            )
        }
        return
    }

    val riwayatPemeriksaan = remember(warga) {
        viewModel.getRiwayatPemeriksaan(nik, warga.status)
    }

    Scaffold(
        topBar = {
            PosyanduTopBar(
                title = "Profil Warga",
                canNavigateBack = true,
                onNavigateBack = onNavigateBack
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { onNavigateToCatatPemeriksaan(nik) },
                containerColor = PosyanduPrimary,
                contentColor = Color.White,
                icon = { Icon(Icons.Default.MonitorWeight, contentDescription = null) },
                text = { Text("Catat Pemeriksaan", fontWeight = FontWeight.Bold) }
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

                // Profile Header Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(70.dp)
                                .background(
                                    color = warga.status.badgeColor.copy(alpha = 0.15f),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = warga.namaLengkap.take(2).uppercase(),
                                color = warga.status.badgeColor,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = warga.namaLengkap,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        Text(
                            text = "NIK: ${warga.nik}",
                            fontSize = 13.sp,
                            color = TextSecondary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        CategoryBadge(kategori = warga.status)

                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider(color = BorderColor)
                        Spacer(modifier = Modifier.height(14.dp))

                        // Biodata Key-Value
                        InfoRow(label = "Tanggal Lahir", value = warga.tanggalLahir)
                        InfoRow(label = "Jenis Kelamin", value = warga.jenisKelamin)
                        if (warga.status == KategoriWarga.BALITA || warga.status == KategoriWarga.REMAJA) {
                            InfoRow(label = "Nama Ayah", value = warga.namaAyah)
                            InfoRow(label = "Nama Ibu", value = warga.namaIbu)
                        }
                        InfoRow(label = "Alamat", value = warga.alamat)
                    }
                }
            }

            // Section Header: Riwayat Pemeriksaan
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Riwayat Pemeriksaan & Antropometri",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "${riwayatPemeriksaan.size} Catatan",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }

            if (riwayatPemeriksaan.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "Belum ada riwayat pemeriksaan",
                                    fontWeight = FontWeight.Medium,
                                    color = TextSecondary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Tekan tombol di bawah untuk mencatat data fisik",
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            }
                        }
                    }
                }
            } else {
                items(riwayatPemeriksaan) { item ->
                    PemeriksaanCard(item = item, kategori = warga.status)
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp)) // Space for FAB
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 13.sp, color = TextSecondary)
        Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
    }
}

@Composable
private fun PemeriksaanCard(
    item: PemeriksaanItem,
    kategori: KategoriWarga
) {
    val isWarning = item.status.contains("Stunting", ignoreCase = true) ||
            item.status.contains("KEK", ignoreCase = true) ||
            item.status.contains("Anemia", ignoreCase = true) ||
            item.status.contains("Hipertensi", ignoreCase = true)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "📅 ${item.tanggal}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = TextPrimary
                )

                // Status Badge
                Surface(
                    color = if (isWarning) StatusDanger.copy(alpha = 0.12f) else StatusSuccess.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = item.status,
                        color = if (isWarning) StatusDanger else StatusSuccess,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            when (item) {
                is PemeriksaanBalita -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MetricBox(label = "Berat Badan", value = "${item.bb} kg")
                        MetricBox(label = "Tinggi Badan", value = "${item.tb} cm")
                        MetricBox(label = "Lingkar Kepala", value = "${item.lk} cm")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MetricBox(label = "Lingkar Lengan (LA)", value = "${item.la} cm")
                        MetricBox(label = "Lingkar Perut (LP)", value = "${item.lp} cm")
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                is PemeriksaanBumil -> {
                    Text(
                        text = "Usia Kehamilan: ${item.usiaKehamilan}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PosyanduPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MetricBox(label = "Berat Badan", value = "${item.bb} kg")
                        MetricBox(label = "Tensi Darah", value = item.tensi)
                        MetricBox(label = "LILA (LA)", value = "${item.la} cm")
                    }
                }
                is PemeriksaanRemaja -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MetricBox(label = "Berat / Tinggi", value = "${item.bb}kg / ${item.tb}cm")
                        MetricBox(label = "Hemoglobin (Hb)", value = "${item.hemoglobin} g/dL")
                        MetricBox(label = "Tensi", value = item.tensi)
                    }
                }
                is PemeriksaanLansia -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MetricBox(label = "Tensi Darah", value = item.tensi)
                        MetricBox(label = "Gula Darah", value = "${item.gula} mg/dL")
                        MetricBox(label = "BB / TB", value = "${item.bb}kg / ${item.tb}cm")
                    }
                    if (item.riwayatPenyakit.isNotBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Riwayat: ${item.riwayatPenyakit}",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MetricBox(label: String, value: String) {
    Column {
        Text(text = label, fontSize = 11.sp, color = TextSecondary)
        Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
    }
}

