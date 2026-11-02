package com.example.mobilee_posyandu.ui.features.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilee_posyandu.data.model.JadwalPosyandu
import com.example.mobilee_posyandu.data.model.KategoriWarga
import com.example.mobilee_posyandu.data.model.Warga
import com.example.mobilee_posyandu.data.repository.IPosyanduRepository
import com.example.mobilee_posyandu.data.repository.PosyanduRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class DashboardUiState(
    val currentUser: String = "Petugas Posyandu",
    val countBalita: Int = 0,
    val countBumil: Int = 0,
    val countRemaja: Int = 0,
    val countLansia: Int = 0,
    val recentWarga: List<Warga> = emptyList(),
    val nextJadwal: JadwalPosyandu? = null,
    val isOnline: Boolean = false,
    val isRefreshing: Boolean = false
)

class DashboardViewModel(
    private val repository: IPosyanduRepository = PosyanduRepository.getInstance()
) : ViewModel() {

    val uiState: StateFlow<DashboardUiState> = combine(
        repository.currentUser,
        repository.wargaList,
        repository.jadwalList,
        repository.isOnline
    ) { user, wargaList, jadwalList, isOnline ->
        DashboardUiState(
            currentUser = user ?: "Petugas Posyandu",
            countBalita = wargaList.count { it.status == KategoriWarga.BALITA },
            countBumil = wargaList.count { it.status == KategoriWarga.IBU_HAMIL },
            countRemaja = wargaList.count { it.status == KategoriWarga.REMAJA },
            countLansia = wargaList.count { it.status == KategoriWarga.LANSIA },
            recentWarga = wargaList.take(5),
            nextJadwal = jadwalList.firstOrNull(),
            isOnline = isOnline,
            isRefreshing = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardUiState()
    )

    fun refresh() {
        viewModelScope.launch {
            repository.refreshFromApi()
        }
    }

    fun logout(onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.logout()
            onSuccess()
        }
    }
}

