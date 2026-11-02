package com.example.mobilee_posyandu.ui.features.jadwal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobilee_posyandu.data.repository.IPosyanduRepository
import com.example.mobilee_posyandu.data.repository.PosyanduRepository
import com.example.mobilee_posyandu.ui.components.PosyanduTopBar
import com.example.mobilee_posyandu.ui.theme.PosyanduPrimary
import com.example.mobilee_posyandu.ui.theme.TextPrimary
import com.example.mobilee_posyandu.ui.theme.TextSecondary

@Composable
fun JadwalScreen(
    onNavigateBack: () -> Unit,
    repository: IPosyanduRepository = PosyanduRepository.getInstance()
) {
    val jadwalList by repository.jadwalList.collectAsState()

    Scaffold(
        topBar = {
            PosyanduTopBar(
                title = "Jadwal Kegiatan Posyandu",
                canNavigateBack = true,
                onNavigateBack = onNavigateBack
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8FAFC))
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "Agenda Pelayanan Mendatang",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Jadwal pelayanan rutin balita, ibu hamil, remaja, dan lansia",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            items(jadwalList, key = { it.idJadwal }) { jadwal ->
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
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CalendarMonth,
                                    contentDescription = null,
                                    tint = PosyanduPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = jadwal.tanggalPelaksanaan,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = PosyanduPrimary
                                )
                            }

                            Surface(
                                color = Color(0xFFFEF3C7),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "Rencana",
                                    color = Color(0xFFD97706),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = jadwal.kegiatanPelaksanaan,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = TextSecondary,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = jadwal.lokasi,
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }
        }
    }
}

