package com.example.mobilee_posyandu.ui.features.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobilee_posyandu.data.repository.PosyanduRepository
import com.example.mobilee_posyandu.ui.theme.PosyanduPrimary
import com.example.mobilee_posyandu.ui.theme.TextPrimary
import com.example.mobilee_posyandu.ui.theme.TextSecondary

import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var password by remember { mutableStateOf("password") }

    val roles = listOf("Kader Posyandu", "Bidan Desa", "Ketua / Admin")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        // Logo Icon
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(
                    color = PosyanduPrimary.copy(alpha = 0.12f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.HealthAndSafety,
                contentDescription = "Logo e-Posyandu",
                tint = PosyanduPrimary,
                modifier = Modifier.size(48.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "e-Posyandu Mobile",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        Text(
            text = "Pelayanan Kesehatan Terpadu Masyarakat",
            fontSize = 13.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Login Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Masuk ke Akun",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Role Selector Chips
                Text(
                    text = "Pilih Peran:",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    roles.forEach { role ->
                        val isSelected = uiState.role == role
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.selectRole(role) },
                            label = {
                                Text(
                                    text = role.substringBefore(" "),
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PosyanduPrimary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Username field
                OutlinedTextField(
                    value = uiState.username,
                    onValueChange = { viewModel.updateUsername(it) },
                    label = { Text("Username") },
                    leadingIcon = {
                        Icon(Icons.Default.Person, contentDescription = null, tint = PosyanduPrimary)
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Password field
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = null, tint = PosyanduPrimary)
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                if (uiState.errorMessage != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = uiState.errorMessage ?: "",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        viewModel.login(password = password, onSuccess = onLoginSuccess)
                    },
                    enabled = !uiState.isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PosyanduPrimary)
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(22.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "Masuk Sistem",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Demo Accounts Guide Info
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "💡 Akun Uji Coba Default (Seeder):",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "• Kader Posyandu: kader1 / password\n• Bidan Desa: bidan_desa / password\n• Ketua: ketua / password",
                    fontSize = 11.sp,
                    color = TextSecondary,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

