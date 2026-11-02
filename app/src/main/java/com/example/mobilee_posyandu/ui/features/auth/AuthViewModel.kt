package com.example.mobilee_posyandu.ui.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilee_posyandu.data.repository.IPosyanduRepository
import com.example.mobilee_posyandu.data.repository.PosyanduRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthUiState(
    val username: String = "kader1",
    val role: String = "Kader Posyandu",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)

class AuthViewModel(
    private val repository: IPosyanduRepository = PosyanduRepository.getInstance()
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun updateUsername(value: String) {
        _uiState.update { it.copy(username = value, errorMessage = null) }
    }

    fun selectRole(role: String) {
        val defaultUsername = when (role) {
            "Kader Posyandu" -> "kader1"
            "Bidan Desa" -> "bidan_desa"
            "Ketua / Admin" -> "ketua"
            else -> "kader1"
        }
        _uiState.update { it.copy(role = role, username = defaultUsername, errorMessage = null) }
    }

    fun login(password: String = "password", onSuccess: () -> Unit) {
        val current = _uiState.value
        if (current.username.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Username tidak boleh kosong") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = repository.login(current.username, current.role, password)
            if (result.isSuccess) {
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                onSuccess()
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Login gagal: ${result.getOrNull() ?: "Periksa koneksi"}"
                    )
                }
            }
        }
    }
}

