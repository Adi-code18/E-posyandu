package com.example.mobilee_posyandu.data.repository

import android.util.Log
import com.example.mobilee_posyandu.data.model.*
import com.example.mobilee_posyandu.data.remote.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PosyanduRepository private constructor() {

    private val apiService = ApiConfig.getApiService()
    private val scope = CoroutineScope(Dispatchers.IO)

    private val _currentUser = MutableStateFlow<String?>("Ibu Kader Anggrek (Kader)")
    val currentUser: StateFlow<String?> = _currentUser.asStateFlow()

    private val _authToken = MutableStateFlow<String?>(null)
    val authToken: StateFlow<String?> = _authToken.asStateFlow()

    private val _wargaList = MutableStateFlow<List<Warga>>(MockData.wargaList.toList())
    val wargaList: StateFlow<List<Warga>> = _wargaList.asStateFlow()

    private val _balitaPemeriksaan = MutableStateFlow<List<PemeriksaanBalita>>(MockData.pemeriksaanBalitaList.toList())
    val balitaPemeriksaan: StateFlow<List<PemeriksaanBalita>> = _balitaPemeriksaan.asStateFlow()

    private val _bumilPemeriksaan = MutableStateFlow<List<PemeriksaanBumil>>(MockData.pemeriksaanBumilList.toList())
    val bumilPemeriksaan: StateFlow<List<PemeriksaanBumil>> = _bumilPemeriksaan.asStateFlow()

    private val _remajaPemeriksaan = MutableStateFlow<List<PemeriksaanRemaja>>(MockData.pemeriksaanRemajaList.toList())
    val remajaPemeriksaan: StateFlow<List<PemeriksaanRemaja>> = _remajaPemeriksaan.asStateFlow()

    private val _lansiaPemeriksaan = MutableStateFlow<List<PemeriksaanLansia>>(MockData.pemeriksaanLansiaList.toList())
    val lansiaPemeriksaan: StateFlow<List<PemeriksaanLansia>> = _lansiaPemeriksaan.asStateFlow()

    private val _jadwalList = MutableStateFlow<List<JadwalPosyandu>>(MockData.jadwalList.toList())
    val jadwalList: StateFlow<List<JadwalPosyandu>> = _jadwalList.asStateFlow()

    private val _informasiList = MutableStateFlow<List<InformasiUmum>>(MockData.informasiList.toList())
    val informasiList: StateFlow<List<InformasiUmum>> = _informasiList.asStateFlow()

    private val _isOnline = MutableStateFlow(false)
    val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

    init {
        // Otomatis coba sinkronkan data saat aplikasi dimulai
        refreshFromApi()
    }

    /**
     * Memuat data terbaru dari Laravel REST API dengan fallback ke data lokal jika server offline.
     */
    fun refreshFromApi() {
        scope.launch {
            try {
                // 1. Fetch Warga
                val responseWarga = apiService.getWarga(perPage = 100)
                if (responseWarga.isSuccessful && responseWarga.body()?.data != null) {
                    val apiWargaList = responseWarga.body()!!.data!!.data.map { dto ->
                        Warga(
                            nik = dto.nik,
                            namaLengkap = dto.namaLengkap,
                            tanggalLahir = dto.tanggalLahir,
                            jenisKelamin = dto.jenisKelamin,
                            namaAyah = dto.namaAyah ?: "-",
                            namaIbu = dto.namaIbu ?: "-",
                            status = KategoriWarga.fromString(dto.status ?: "Balita"),
                            alamat = "Desa Sejahtera RT 02 / RW 01"
                        )
                    }
                    if (apiWargaList.isNotEmpty()) {
                        _wargaList.value = apiWargaList
                        _isOnline.value = true
                    }
                }

                // 2. Fetch Jadwal
                val responseJadwal = apiService.getJadwal()
                if (responseJadwal.isSuccessful && responseJadwal.body()?.data != null) {
                    val apiJadwal = responseJadwal.body()!!.data!!.map { dto ->
                        JadwalPosyandu(
                            idJadwal = dto.idJadwal,
                            tanggalPelaksanaan = dto.tanggalPelaksanaan,
                            kegiatanPelaksanaan = dto.kegiatanPelaksanaan,
                            statusPelaksanaan = dto.statusPelaksanaan
                        )
                    }
                    if (apiJadwal.isNotEmpty()) {
                        _jadwalList.value = apiJadwal
                    }
                }

                // 3. Fetch Artikel
                val responseArtikel = apiService.getArtikel()
                if (responseArtikel.isSuccessful && responseArtikel.body()?.data != null) {
                    val apiArtikel = responseArtikel.body()!!.data!!.map { dto ->
                        InformasiUmum(
                            idInformasi = dto.idInformasi,
                            judul = dto.judul,
                            isiInformasi = dto.isiInformasi,
                            tanggalPost = dto.tanggalPost,
                            kategoriInfo = dto.kategoriInfo
                        )
                    }
                    if (apiArtikel.isNotEmpty()) {
                        _informasiList.value = apiArtikel
                    }
                }
            } catch (e: Exception) {
                Log.w("PosyanduRepository", "Backend belum aktif atau offline, menggunakan cache lokal: ${e.message}")
                _isOnline.value = false
            }
        }
    }

    /**
     * Login autentikasi ke Laravel REST API (dengan fallback akun demo seeder)
     */
    fun login(username: String, role: String): Boolean {
        _currentUser.value = "$username ($role)"

        // Jalankan background login ke API
        scope.launch {
            try {
                val response = apiService.login(LoginRequest(username = username, password = "password"))
                if (response.isSuccessful && response.body()?.data != null) {
                    val loginData = response.body()!!.data!!
                    _authToken.value = loginData.token
                    _isOnline.value = true
                    refreshFromApi()
                }
            } catch (e: Exception) {
                Log.w("PosyanduRepository", "Login remote gagal, melanjutkan sesi lokal: ${e.message}")
            }
        }
        return true
    }

    fun logout() {
        scope.launch {
            try {
                apiService.logout()
            } catch (_: Exception) {}
        }
        _currentUser.value = null
        _authToken.value = null
    }

    fun getWargaByNik(nik: String): Warga? {
        return _wargaList.value.find { it.nik == nik }
    }

    fun tambahWarga(warga: Warga): Boolean {
        if (_wargaList.value.any { it.nik == warga.nik }) {
            return false // NIK sudah terdaftar
        }
        _wargaList.value = listOf(warga) + _wargaList.value

        // Sinkronisasi ke REST API Laravel
        scope.launch {
            try {
                val req = WargaStoreRequest(
                    nik = warga.nik,
                    namaLengkap = warga.namaLengkap,
                    tanggalLahir = warga.tanggalLahir,
                    jenisKelamin = warga.jenisKelamin,
                    namaAyah = warga.namaAyah,
                    namaIbu = warga.namaIbu,
                    status = warga.status.label
                )
                val resp = apiService.createWarga(req)
                if (resp.isSuccessful) {
                    _isOnline.value = true
                    Log.i("PosyanduRepository", "Warga berhasil dikirim ke Laravel API: ${warga.namaLengkap}")
                }
            } catch (e: Exception) {
                Log.w("PosyanduRepository", "Gagal sync warga ke API: ${e.message}")
            }
        }
        return true
    }

    fun tambahPemeriksaanBalita(item: PemeriksaanBalita) {
        _balitaPemeriksaan.value = listOf(item) + _balitaPemeriksaan.value

        scope.launch {
            try {
                val req = PemeriksaanBalitaRequest(
                    tanggal = item.tanggal,
                    bb = item.bb,
                    tb = item.tb,
                    lk = item.lk,
                    la = item.la,
                    lp = item.lp,
                    status = item.status
                )
                apiService.submitBalita(req)
            } catch (e: Exception) {
                Log.w("PosyanduRepository", "Gagal sync pemeriksaan balita: ${e.message}")
            }
        }
    }

    fun tambahPemeriksaanBumil(item: PemeriksaanBumil) {
        _bumilPemeriksaan.value = listOf(item) + _bumilPemeriksaan.value

        scope.launch {
            try {
                val req = PemeriksaanBumilRequest(
                    tanggal = item.tanggal,
                    usiaKehamilan = item.usiaKehamilan,
                    bb = item.bb,
                    tb = item.tb,
                    la = item.la,
                    tensi = item.tensi,
                    status = item.status
                )
                apiService.submitBumil(req)
            } catch (e: Exception) {
                Log.w("PosyanduRepository", "Gagal sync pemeriksaan bumil: ${e.message}")
            }
        }
    }

    fun tambahPemeriksaanRemaja(item: PemeriksaanRemaja) {
        _remajaPemeriksaan.value = listOf(item) + _remajaPemeriksaan.value

        scope.launch {
            try {
                val req = PemeriksaanRemajaRequest(
                    tanggal = item.tanggal,
                    bb = item.bb,
                    tb = item.tb,
                    tensi = item.tensi,
                    hemoglobin = item.hemoglobin,
                    gula = item.gula,
                    status = item.status
                )
                apiService.submitRemaja(req)
            } catch (e: Exception) {
                Log.w("PosyanduRepository", "Gagal sync pemeriksaan remaja: ${e.message}")
            }
        }
    }

    fun tambahPemeriksaanLansia(item: PemeriksaanLansia) {
        _lansiaPemeriksaan.value = listOf(item) + _lansiaPemeriksaan.value

        scope.launch {
            try {
                val req = PemeriksaanLansiaRequest(
                    tanggal = item.tanggal,
                    bb = item.bb,
                    tb = item.tb,
                    tensi = item.tensi,
                    gula = item.gula,
                    riwayatPenyakit = item.riwayatPenyakit,
                    status = item.status
                )
                apiService.submitLansia(req)
            } catch (e: Exception) {
                Log.w("PosyanduRepository", "Gagal sync pemeriksaan lansia: ${e.message}")
            }
        }
    }

    fun getPemeriksaanByNik(nik: String, kategori: KategoriWarga): List<PemeriksaanItem> {
        return when (kategori) {
            KategoriWarga.BALITA -> _balitaPemeriksaan.value.filter { it.nik == nik }
            KategoriWarga.IBU_HAMIL -> _bumilPemeriksaan.value.filter { it.nik == nik }
            KategoriWarga.REMAJA -> _remajaPemeriksaan.value.filter { it.nik == nik }
            KategoriWarga.LANSIA -> _lansiaPemeriksaan.value.filter { it.nik == nik }
        }
    }

    companion object {
        @Volatile
        private var instance: PosyanduRepository? = null

        fun getInstance(): PosyanduRepository {
            return instance ?: synchronized(this) {
                instance ?: PosyanduRepository().also { instance = it }
            }
        }
    }
}
