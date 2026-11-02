package com.example.mobilee_posyandu.ui.features.warga

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobilee_posyandu.data.model.KategoriWarga
import com.example.mobilee_posyandu.data.repository.PosyanduRepository
import com.example.mobilee_posyandu.ui.components.PosyanduTopBar
import com.example.mobilee_posyandu.ui.components.WargaItemCard
import com.example.mobilee_posyandu.ui.theme.PosyanduPrimary
import com.example.mobilee_posyandu.ui.theme.TextPrimary
import com.example.mobilee_posyandu.ui.theme.TextSecondary

import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun WargaListScreen(
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (String) -> Unit,
    onNavigateToTambah: () -> Unit,
    viewModel: WargaViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            PosyanduTopBar(
                title = "Data Anggota Warga",
                canNavigateBack = true,
                onNavigateBack = onNavigateBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToTambah,
                containerColor = PosyanduPrimary,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Warga")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8FAFC))
                .padding(innerPadding)
        ) {
            // Search Input
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = { viewModel.onSearchQueryChange(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Cari nama atau NIK 16 digit...") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null, tint = PosyanduPrimary)
                },
                trailingIcon = {
                    if (uiState.searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.onSearchQueryChange("") }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PosyanduPrimary,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                )
            )

            // Category Filter Horizontal Scroll
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = uiState.selectedCategory == null,
                    onClick = { viewModel.onCategorySelect(null) },
                    label = { Text("Semua (${uiState.wargaList.size})") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = PosyanduPrimary,
                        selectedLabelColor = Color.White
                    )
                )

                KategoriWarga.entries.forEach { kategori ->
                    val isSelected = uiState.selectedCategory == kategori
                    val count = uiState.wargaList.count { it.status == kategori }
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.onCategorySelect(kategori) },
                        label = { Text("${kategori.label} ($count)") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = kategori.badgeColor,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Warga List
            if (uiState.filteredList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Tidak ada warga ditemukan",
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Coba ubah kata kunci atau filter kategori",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(uiState.filteredList, key = { it.nik }) { warga ->
                        WargaItemCard(
                            warga = warga,
                            onClick = { onNavigateToDetail(warga.nik) }
                        )
                    }
                }
            }
        }
    }
}

