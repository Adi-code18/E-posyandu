package com.example.mobilee_posyandu.ui.features.status

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobilee_posyandu.ui.components.HttpStatusCategory
import com.example.mobilee_posyandu.ui.components.HttpStatusCodeInfo
import com.example.mobilee_posyandu.ui.components.HttpStatusLandingView
import com.example.mobilee_posyandu.ui.components.PosyanduTopBar
import com.example.mobilee_posyandu.ui.theme.PosyanduPrimary
import com.example.mobilee_posyandu.ui.theme.TextPrimary
import com.example.mobilee_posyandu.ui.theme.TextSecondary

@Composable
fun StatusCodeShowcaseScreen(
    onNavigateBack: () -> Unit
) {
    var selectedCode by remember { mutableStateOf(HttpStatusCodeInfo.NOT_FOUND_404) }
    var selectedCategoryFilter by remember { mutableStateOf<HttpStatusCategory?>(null) }
    var snackbarMessage by remember { mutableStateOf<String?>(null) }

    val filteredCodes = remember(selectedCategoryFilter) {
        if (selectedCategoryFilter == null) {
            HttpStatusCodeInfo.entries
        } else {
            HttpStatusCodeInfo.entries.filter { it.category == selectedCategoryFilter }
        }
    }

    Scaffold(
        topBar = {
            PosyanduTopBar(
                title = "Katalog Error & Status Code",
                canNavigateBack = true,
                onNavigateBack = onNavigateBack
            )
        },
        snackbarHost = {
            snackbarMessage?.let { msg ->
                Snackbar(
                    modifier = Modifier.padding(16.dp),
                    action = {
                        TextButton(onClick = { snackbarMessage = null }) {
                            Text("OK", color = Color.White)
                        }
                    }
                ) {
                    Text(msg)
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8FAFC))
                .padding(innerPadding)
        ) {
            // Category Filter Horizontal Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedCategoryFilter == null,
                    onClick = { selectedCategoryFilter = null },
                    label = { Text("Semua Code (${HttpStatusCodeInfo.entries.size})") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = PosyanduPrimary,
                        selectedLabelColor = Color.White
                    )
                )

                HttpStatusCategory.entries.forEach { cat ->
                    val isSelected = selectedCategoryFilter == cat
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategoryFilter = if (isSelected) null else cat },
                        label = { Text(cat.title) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = cat.badgeColor,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            // Quick Code Selector Pills
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filteredCodes.forEach { item ->
                    val isSelected = selectedCode == item
                    val themeColor = item.category.badgeColor

                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { selectedCode = item },
                        color = if (isSelected) themeColor else Color.White,
                        shape = RoundedCornerShape(12.dp),
                        shadowElevation = if (isSelected) 3.dp else 1.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(
                                        if (isSelected) Color.White else themeColor,
                                        CircleShape
                                    )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = item.code,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else TextPrimary
                            )
                        }
                    }
                }
            }

            HorizontalDivider(color = Color(0xFFE2E8F0), modifier = Modifier.padding(top = 8.dp))

            // Live Landing View Render
            Box(modifier = Modifier.weight(1f)) {
                HttpStatusLandingView(
                    statusInfo = selectedCode,
                    onPrimaryAction = {
                        snackbarMessage = "Aksi: '${selectedCode.primaryActionText}' ditekan untuk status ${selectedCode.code}"
                    },
                    onSecondaryAction = {
                        if (selectedCode.secondaryActionText == "Kembali ke Beranda") {
                            onNavigateBack()
                        } else {
                            snackbarMessage = "Aksi: '${selectedCode.secondaryActionText}' ditekan"
                        }
                    }
                )
            }
        }
    }
}

