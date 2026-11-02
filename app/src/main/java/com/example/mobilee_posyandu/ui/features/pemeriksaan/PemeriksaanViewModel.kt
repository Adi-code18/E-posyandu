package com.example.mobilee_posyandu.ui.features.pemeriksaan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilee_posyandu.data.model.*
import com.example.mobilee_posyandu.data.repository.IPosyanduRepository
import com.example.mobilee_posyandu.data.repository.PosyanduRepository
import com.example.mobilee_posyandu.data.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PemeriksaanUiState(
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)

class PemeriksaanViewModel(
    private val repository: IPosyanduRepository = PosyanduRepository.getInstance()
) : ViewModel() {

    private val _uiState = MutableStateFlow(PemeriksaanUiState())
    val uiState: StateFlow<PemeriksaanUiState> = _uiState.asStateFlow()

    fun getWarga(nik: String): Warga? = repository.getWargaByNik(nik)

    fun submitBalita(item: PemeriksaanBalita, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, errorMessage = null) }
            val res = repository.tambahPemeriksaanBalita(item)
            handleResult(res, onSuccess)
        }
    }

    fun submitBumil(item: PemeriksaanBumil, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, errorMessage = null) }
            val res = repository.tambahPemeriksaanBumil(item)
            handleResult(res, onSuccess)
        }
    }

    fun submitRemaja(item: PemeriksaanRemaja, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, errorMessage = null) }
            val res = repository.tambahPemeriksaanRemaja(item)
            handleResult(res, onSuccess)
        }
    }

    fun submitLansia(item: PemeriksaanLansia, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, errorMessage = null) }
            val res = repository.tambahPemeriksaanLansia(item)
            handleResult(res, onSuccess)
        }
    }

    private fun <T> handleResult(res: Resource<T>, onSuccess: () -> Unit) {
        when (res) {
            is Resource.Success -> {
                _uiState.update { it.copy(isSubmitting = false, isSuccess = true) }
                onSuccess()
            }
            is Resource.Error -> {
                _uiState.update { it.copy(isSubmitting = false, errorMessage = res.message ?: "Gagal mencatat pemeriksaan") }
            }
            else -> {}
        }
    }
}

