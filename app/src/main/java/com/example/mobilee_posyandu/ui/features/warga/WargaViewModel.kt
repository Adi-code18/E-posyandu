package com.example.mobilee_posyandu.ui.features.warga

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilee_posyandu.data.model.KategoriWarga
import com.example.mobilee_posyandu.data.model.PemeriksaanItem
import com.example.mobilee_posyandu.data.model.Warga
import com.example.mobilee_posyandu.data.repository.IPosyanduRepository
import com.example.mobilee_posyandu.data.repository.PosyanduRepository
import com.example.mobilee_posyandu.data.util.Resource
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class WargaListUiState(
    val wargaList: List<Warga> = emptyList(),
    val filteredList: List<Warga> = emptyList(),
    val searchQuery: String = "",
    val selectedCategory: KategoriWarga? = null,
    val isLoading: Boolean = false
)

data class FormWargaUiState(
    val nik: String = "",
    val namaLengkap: String = "",
    val tanggalLahir: String = "",
    val jenisKelamin: String = "Laki-laki",
    val namaAyah: String = "",
    val namaIbu: String = "",
    val alamat: String = "Dusun Anggrek RT 01 / RW 02",
    val status: KategoriWarga = KategoriWarga.BALITA,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class WargaViewModel(
    private val repository: IPosyanduRepository = PosyanduRepository.getInstance()
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow<KategoriWarga?>(null)
    val selectedCategory: StateFlow<KategoriWarga?> = _selectedCategory.asStateFlow()

    val uiState: StateFlow<WargaListUiState> = combine(
        repository.wargaList,
        _searchQuery,
        _selectedCategory
    ) { allWarga, query, category ->
        val filtered = allWarga.filter { warga ->
            val matchQuery = query.isBlank() ||
                    warga.namaLengkap.contains(query, ignoreCase = true) ||
                    warga.nik.contains(query)
            val matchCat = category == null || warga.status == category
            matchQuery && matchCat
        }
        WargaListUiState(
            wargaList = allWarga,
            filteredList = filtered,
            searchQuery = query,
            selectedCategory = category,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = WargaListUiState()
    )

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onCategorySelect(category: KategoriWarga?) {
        _selectedCategory.value = if (_selectedCategory.value == category) null else category
    }

    fun getWargaByNik(nik: String): Warga? {
        return repository.getWargaByNik(nik)
    }

    fun getRiwayatPemeriksaan(nik: String, kategori: KategoriWarga): List<PemeriksaanItem> {
        return repository.getPemeriksaanByNik(nik, kategori)
    }

    fun simpanWargaBaru(warga: Warga, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val result = repository.tambahWarga(warga)
            when (result) {
                is Resource.Success -> onSuccess()
                is Resource.Error -> onError(result.message ?: "Gagal menyimpan warga")
                else -> {}
            }
        }
    }
}

