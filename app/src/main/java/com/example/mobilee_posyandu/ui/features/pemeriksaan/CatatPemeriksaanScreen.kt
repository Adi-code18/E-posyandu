package com.example.mobilee_posyandu.ui.features.pemeriksaan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobilee_posyandu.data.model.*
import com.example.mobilee_posyandu.data.repository.PosyanduRepository
import com.example.mobilee_posyandu.ui.components.CategoryBadge
import com.example.mobilee_posyandu.ui.components.PosyanduTopBar
import com.example.mobilee_posyandu.ui.theme.PosyanduPrimary
import com.example.mobilee_posyandu.ui.theme.TextPrimary
import com.example.mobilee_posyandu.ui.theme.TextSecondary

@Composable
fun CatatPemeriksaanScreen(
    nik: String,
    onNavigateBack: () -> Unit,
    repository: PosyanduRepository = PosyanduRepository.getInstance()
) {
    val wargaList by repository.wargaList.collectAsState()
    val warga = wargaList.find { it.nik == nik }

    if (warga == null) {
        onNavigateBack()
        return
    }

    var tanggal by remember { mutableStateOf("31 Oktober 2026") }
    var bb by remember { mutableStateOf("") }
    var tb by remember { mutableStateOf("") }

    // Balita specific
    var lk by remember { mutableStateOf("") }
    var la by remember { mutableStateOf("") }
    var lp by remember { mutableStateOf("") }
    var statusBalita by remember { mutableStateOf("Gizi Baik (Normal)") }

    // Bumil specific
    var usiaKehamilan by remember { mutableStateOf("Trimester 2 (20 Minggu)") }
    var tensiBumil by remember { mutableStateOf("115/75") }
    var lilaBumil by remember { mutableStateOf("") }
    var statusBumil by remember { mutableStateOf("Normal Sehat") }

    // Remaja specific
    var tensiRemaja by remember { mutableStateOf("110/70") }
    var hbRemaja by remember { mutableStateOf("12.5") }
    var gulaRemaja by remember { mutableStateOf("90") }
    var statusRemaja by remember { mutableStateOf("Normal") }

    // Lansia specific
    var tensiLansia by remember { mutableStateOf("130/85") }
    var gulaLansia by remember { mutableStateOf("120") }
    var riwayatPenyakit by remember { mutableStateOf("") }
    var statusLansia by remember { mutableStateOf("Terkontrol") }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            PosyanduTopBar(
                title = "Catat Pemeriksaan Fisik",
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
            // Header Info Warga
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(14.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = warga.namaLengkap,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = TextPrimary
                        )
                        Text(
                            text = "NIK: ${warga.nik}",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                    CategoryBadge(kategori = warga.status)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Form Content
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Formulir Pemeriksaan (${warga.status.label})",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Tanggal Periksa
                    OutlinedTextField(
                        value = tanggal,
                        onValueChange = { tanggal = it },
                        label = { Text("Tanggal Pemeriksaan") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // BB & TB
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = bb,
                            onValueChange = { bb = it },
                            label = { Text("Berat Badan (kg)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        OutlinedTextField(
                            value = tb,
                            onValueChange = { tb = it },
                            label = { Text("Tinggi Badan (cm)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    when (warga.status) {
                        KategoriWarga.BALITA -> {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedTextField(
                                    value = lk,
                                    onValueChange = { lk = it },
                                    label = { Text("Lingkar Kepala (cm)") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    singleLine = true,
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                OutlinedTextField(
                                    value = la,
                                    onValueChange = { la = it },
                                    label = { Text("Lingkar Lengan (cm)") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    singleLine = true,
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = lp,
                                onValueChange = { lp = it },
                                label = { Text("Lingkar Perut (cm)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = statusBalita,
                                onValueChange = { statusBalita = it },
                                label = { Text("Status Gizi / Analisis") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }

                        KategoriWarga.IBU_HAMIL -> {
                            OutlinedTextField(
                                value = usiaKehamilan,
                                onValueChange = { usiaKehamilan = it },
                                label = { Text("Usia Kehamilan (Contoh: Trimester 2)") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedTextField(
                                    value = tensiBumil,
                                    onValueChange = { tensiBumil = it },
                                    label = { Text("Tensi Darah") },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                OutlinedTextField(
                                    value = lilaBumil,
                                    onValueChange = { lilaBumil = it },
                                    label = { Text("LILA (cm)") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    singleLine = true,
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = statusBumil,
                                onValueChange = { statusBumil = it },
                                label = { Text("Status Kehamilan") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }

                        KategoriWarga.REMAJA -> {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedTextField(
                                    value = tensiRemaja,
                                    onValueChange = { tensiRemaja = it },
                                    label = { Text("Tensi Darah") },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                OutlinedTextField(
                                    value = hbRemaja,
                                    onValueChange = { hbRemaja = it },
                                    label = { Text("Hb (g/dL)") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    singleLine = true,
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = gulaRemaja,
                                onValueChange = { gulaRemaja = it },
                                label = { Text("Gula Darah Sewaktu (mg/dL)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = statusRemaja,
                                onValueChange = { statusRemaja = it },
                                label = { Text("Status Skrining") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }

                        KategoriWarga.LANSIA -> {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedTextField(
                                    value = tensiLansia,
                                    onValueChange = { tensiLansia = it },
                                    label = { Text("Tensi Darah") },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                OutlinedTextField(
                                    value = gulaLansia,
                                    onValueChange = { gulaLansia = it },
                                    label = { Text("Gula Darah (mg/dL)") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    singleLine = true,
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = riwayatPenyakit,
                                onValueChange = { riwayatPenyakit = it },
                                label = { Text("Riwayat Penyakit / Keluhan") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = statusLansia,
                                onValueChange = { statusLansia = it },
                                label = { Text("Evaluasi / Status") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }

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
                            val bbVal = bb.toDoubleOrNull() ?: 0.0
                            val tbVal = tb.toDoubleOrNull() ?: 0.0

                            if (bbVal <= 0.0 || tbVal <= 0.0) {
                                errorMessage = "Berat badan dan tinggi badan harus diisi angka valid"
                                return@Button
                            }

                            val newId = System.currentTimeMillis()

                            when (warga.status) {
                                KategoriWarga.BALITA -> {
                                    repository.tambahPemeriksaanBalita(
                                        PemeriksaanBalita(
                                            idPeriksa = newId,
                                            nik = nik,
                                            tanggal = tanggal,
                                            bb = bbVal,
                                            tb = tbVal,
                                            lk = lk.toDoubleOrNull() ?: 0.0,
                                            la = la.toDoubleOrNull() ?: 0.0,
                                            lp = lp.toDoubleOrNull() ?: 0.0,
                                            status = statusBalita.ifBlank { "Gizi Baik (Normal)" }
                                        )
                                    )
                                }
                                KategoriWarga.IBU_HAMIL -> {
                                    repository.tambahPemeriksaanBumil(
                                        PemeriksaanBumil(
                                            idPeriksa = newId,
                                            nik = nik,
                                            tanggal = tanggal,
                                            usiaKehamilan = usiaKehamilan,
                                            bb = bbVal,
                                            tb = tbVal,
                                            la = lilaBumil.toDoubleOrNull() ?: 0.0,
                                            lp = 0.0,
                                            tensi = tensiBumil,
                                            status = statusBumil.ifBlank { "Normal" }
                                        )
                                    )
                                }
                                KategoriWarga.REMAJA -> {
                                    repository.tambahPemeriksaanRemaja(
                                        PemeriksaanRemaja(
                                            idPeriksa = newId,
                                            nik = nik,
                                            tanggal = tanggal,
                                            bb = bbVal,
                                            tb = tbVal,
                                            tensi = tensiRemaja,
                                            hemoglobin = hbRemaja.toDoubleOrNull() ?: 0.0,
                                            gula = gulaRemaja.toDoubleOrNull() ?: 0.0,
                                            status = statusRemaja.ifBlank { "Normal" }
                                        )
                                    )
                                }
                                KategoriWarga.LANSIA -> {
                                    repository.tambahPemeriksaanLansia(
                                        PemeriksaanLansia(
                                            idPeriksa = newId,
                                            nik = nik,
                                            tanggal = tanggal,
                                            bb = bbVal,
                                            tb = tbVal,
                                            tensi = tensiLansia,
                                            gula = gulaLansia.toDoubleOrNull() ?: 0.0,
                                            riwayatPenyakit = riwayatPenyakit,
                                            status = statusLansia.ifBlank { "Terkontrol" }
                                        )
                                    )
                                }
                            }
                            onNavigateBack()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PosyanduPrimary)
                    ) {
                        Text(
                            text = "Simpan Hasil Pemeriksaan",
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

