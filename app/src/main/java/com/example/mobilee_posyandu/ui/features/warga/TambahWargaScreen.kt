package com.example.mobilee_posyandu.ui.features.warga

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobilee_posyandu.data.model.KategoriWarga
import com.example.mobilee_posyandu.data.model.Warga
import com.example.mobilee_posyandu.data.repository.PosyanduRepository
import com.example.mobilee_posyandu.ui.components.PosyanduTopBar
import com.example.mobilee_posyandu.ui.theme.PosyanduPrimary
import com.example.mobilee_posyandu.ui.theme.TextPrimary
import com.example.mobilee_posyandu.ui.theme.TextSecondary

@Composable
fun TambahWargaScreen(
    onNavigateBack: () -> Unit,
    repository: PosyanduRepository = PosyanduRepository.getInstance()
) {
    var nik by remember { mutableStateOf("") }
    var namaLengkap by remember { mutableStateOf("") }
    var tanggalLahir by remember { mutableStateOf("") }
    var jenisKelamin by remember { mutableStateOf("Laki-laki") }
    var selectedKategori by remember { mutableStateOf(KategoriWarga.BALITA) }
    var namaAyah by remember { mutableStateOf("") }
    var namaIbu by remember { mutableStateOf("") }
    var alamat by remember { mutableStateOf("Dusun Anggrek RT 01 / RW 02") }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isSuccess by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            PosyanduTopBar(
                title = "Pendaftaran Warga Baru",
                canNavigateBack = true,
                onNavigateBack = onNavigateBack
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8FAFC))
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Kategori Sasaran Posyandu",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        KategoriWarga.entries.forEach { kategori ->
                            val isSelected = selectedKategori == kategori
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedKategori = kategori },
                                label = {
                                    Text(
                                        text = kategori.label,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = kategori.badgeColor,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // NIK (16 digit)
                    OutlinedTextField(
                        value = nik,
                        onValueChange = { if (it.length <= 16) nik = it },
                        label = { Text("Nomor Induk Kependudukan (NIK)") },
                        placeholder = { Text("16 Digit NIK") },
                        leadingIcon = {
                            Icon(Icons.Default.Badge, contentDescription = null, tint = PosyanduPrimary)
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Nama Lengkap
                    OutlinedTextField(
                        value = namaLengkap,
                        onValueChange = { namaLengkap = it },
                        label = { Text("Nama Lengkap Warga") },
                        leadingIcon = {
                            Icon(Icons.Default.Person, contentDescription = null, tint = PosyanduPrimary)
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Tanggal Lahir
                    OutlinedTextField(
                        value = tanggalLahir,
                        onValueChange = { tanggalLahir = it },
                        label = { Text("Tanggal Lahir") },
                        placeholder = { Text("Contoh: 15 Mei 2023") },
                        leadingIcon = {
                            Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = PosyanduPrimary)
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Jenis Kelamin
                    Text(
                        text = "Jenis Kelamin:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextSecondary
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = jenisKelamin == "Laki-laki",
                                onClick = { jenisKelamin = "Laki-laki" }
                            )
                            Text("Laki-laki", fontSize = 14.sp)
                        }
                        Spacer(modifier = Modifier.width(20.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = jenisKelamin == "Perempuan",
                                onClick = { jenisKelamin = "Perempuan" }
                            )
                            Text("Perempuan", fontSize = 14.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Nama Orang Tua (jika Balita atau Remaja)
                    if (selectedKategori == KategoriWarga.BALITA || selectedKategori == KategoriWarga.REMAJA) {
                        OutlinedTextField(
                            value = namaAyah,
                            onValueChange = { namaAyah = it },
                            label = { Text("Nama Ayah") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = namaIbu,
                            onValueChange = { namaIbu = it },
                            label = { Text("Nama Ibu") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    // Alamat
                    OutlinedTextField(
                        value = alamat,
                        onValueChange = { alamat = it },
                        label = { Text("Alamat / RT RW") },
                        leadingIcon = {
                            Icon(Icons.Default.Home, contentDescription = null, tint = PosyanduPrimary)
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    if (errorMessage != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = errorMessage ?: "",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            if (nik.length < 5) {
                                errorMessage = "NIK harus diisi dengan benar"
                                return@Button
                            }
                            if (namaLengkap.isBlank()) {
                                errorMessage = "Nama lengkap wajib diisi"
                                return@Button
                            }
                            if (tanggalLahir.isBlank()) {
                                errorMessage = "Tanggal lahir wajib diisi"
                                return@Button
                            }

                            val newWarga = Warga(
                                nik = nik,
                                namaLengkap = namaLengkap,
                                tanggalLahir = tanggalLahir,
                                jenisKelamin = jenisKelamin,
                                namaAyah = namaAyah.ifBlank { "-" },
                                namaIbu = namaIbu.ifBlank { "-" },
                                status = selectedKategori,
                                alamat = alamat.ifBlank { "Desa Sejahtera" }
                            )

                            val success = repository.tambahWarga(newWarga)
                            if (success) {
                                isSuccess = true
                                onNavigateBack()
                            } else {
                                errorMessage = "Warga dengan NIK $nik sudah ada!"
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PosyanduPrimary)
                    ) {
                        Text(
                            text = "Simpan Data Warga",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

