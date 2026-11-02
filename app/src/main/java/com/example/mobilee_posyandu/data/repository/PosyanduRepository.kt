package com.example.mobilee_posyandu.data.repository

import android.util.Log
import com.example.mobilee_posyandu.data.model.*
import com.example.mobilee_posyandu.data.remote.*
import com.example.mobilee_posyandu.data.util.Resource
import com.example.mobilee_posyandu.data.util.toHttpStatusInfo
import com.example.mobilee_posyandu.data.util.toNetworkStatusInfo
import com.example.mobilee_posyandu.ui.components.HttpStatusCodeInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PosyanduRepository private constructor(
    private val apiService: PosyanduApiService = ApiConfig.getApiService()
) : IPosyanduRepository {

    private val scope = CoroutineScope(Dispatchers.IO)

    private val _currentUser = MutableStateFlow<String?>("Ibu Kader Anggrek (Kader)")
    override val currentUser: StateFlow<String?> = _currentUser.asStateFlow()

    private val _authToken = MutableStateFlow<String?>(null)
    override val authToken: StateFlow<String?> = _authToken.asStateFlow()

    private val _wargaList = MutableStateFlow<List<Warga>>(MockData.wargaList.toList())
    override val wargaList: StateFlow<List<Warga>> = _wargaList.asStateFlow()

    private val _balitaPemeriksaan = MutableStateFlow<List<PemeriksaanBalita>>(MockData.pemeriksaanBalitaList.toList())
    override val balitaPemeriksaan: StateFlow<List<PemeriksaanBalita>> = _balitaPemeriksaan.asStateFlow()

    private val _bumilPemeriksaan = MutableStateFlow<List<PemeriksaanBumil>>(MockData.pemeriksaanBumilList.toList())
    override val bumilPemeriksaan: StateFlow<List<PemeriksaanBumil>> = _bumilPemeriksaan.asStateFlow()

    private val _remajaPemeriksaan = MutableStateFlow<List<PemeriksaanRemaja>>(MockData.pemeriksaanRemajaList.toList())
    override val remajaPemeriksaan: StateFlow<List<PemeriksaanRemaja>> = _remajaPemeriksaan.asStateFlow()

    private val _lansiaPemeriksaan = MutableStateFlow<List<PemeriksaanLansia>>(MockData.pemeriksaanLansiaList.toList())
    override val lansiaPemeriksaan: StateFlow<List<PemeriksaanLansia>> = _lansiaPemeriksaan.asStateFlow()

    private val _jadwalList = MutableStateFlow<List<JadwalPosyandu>>(MockData.jadwalList.toList())
    override val jadwalList: StateFlow<List<JadwalPosyandu>> = _jadwalList.asStateFlow()

    private val _informasiList = MutableStateFlow<List<InformasiUmum>>(MockData.informasiList.toList())
    override val informasiList: StateFlow<List<InformasiUmum>> = _informasiList.asStateFlow()

    private val _isOnline = MutableStateFlow(false)
    override val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

    init {
        // Otomatis sinkronkan dari API saat runtime dimulai
        scope.launch {
            refreshFromApi()
        }
    }

    override suspend fun refreshFromApi(): Resource<Unit> = withContext(Dispatchers.IO) {
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

            Resource.Success(Unit)
        } catch (e: Throwable) {
            _isOnline.value = false
            Log.w(TAG, "Backend offline / tidak terjangkau, menggunakan data lokal: ${e.message}")
            Resource.Error(statusInfo = e.toNetworkStatusInfo(), message = e.localizedMessage, cause = e)
        }
    }

    override suspend fun login(username: String, role: String, password: String): Resource<String> = withContext(Dispatchers.IO) {
        _currentUser.value = "$username ($role)"

        try {
            val response = apiService.login(LoginRequest(username = username, password = password))
            if (response.isSuccessful && response.body()?.data != null) {
                val loginData = response.body()!!.data!!
                _authToken.value = loginData.token
                _isOnline.value = true
                refreshFromApi()
                Resource.Success(loginData.token ?: "auth-token-demo")
            } else {
                Resource.Error(statusInfo = response.toHttpStatusInfo(), message = response.message())
            }
        } catch (e: Throwable) {
            Log.w(TAG, "Login remote gagal: ${e.message}, tetap melanjutkan sesi lokal.")
            Resource.Success("local-session-token")
        }
    }

    override suspend fun logout(): Resource<Unit> = withContext(Dispatchers.IO) {
        try {
            apiService.logout()
        } catch (_: Exception) {}
        _currentUser.value = null
        _authToken.value = null
        Resource.Success(Unit)
    }

    override fun getWargaByNik(nik: String): Warga? {
        return _wargaList.value.find { it.nik == nik }
    }

    override suspend fun tambahWarga(warga: Warga): Resource<Warga> = withContext(Dispatchers.IO) {
        if (_wargaList.value.any { it.nik == warga.nik }) {
            return@withContext Resource.Error(
                statusInfo = HttpStatusCodeInfo.UNPROCESSABLE_422,
                message = "NIK ${warga.nik} sudah terdaftar di sistem."
            )
        }

        // Simpan ke memori lokal
        _wargaList.value = listOf(warga) + _wargaList.value

        // Sinkronisasi remote ke Laravel
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
                Resource.Success(warga)
            } else {
                Resource.Error(statusInfo = resp.toHttpStatusInfo(), message = resp.message())
            }
        } catch (e: Throwable) {
            Log.w(TAG, "Gagal kirim warga ke server: ${e.message}, data disimpan lokal.")
            Resource.Success(warga)
        }
    }

    override suspend fun tambahPemeriksaanBalita(item: PemeriksaanBalita): Resource<PemeriksaanBalita> = withContext(Dispatchers.IO) {
        _balitaPemeriksaan.value = listOf(item) + _balitaPemeriksaan.value

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
            val resp = apiService.submitBalita(req)
            if (resp.isSuccessful) Resource.Success(item)
            else Resource.Error(statusInfo = resp.toHttpStatusInfo(), message = resp.message())
        } catch (e: Throwable) {
            Resource.Success(item)
        }
    }

    override suspend fun tambahPemeriksaanBumil(item: PemeriksaanBumil): Resource<PemeriksaanBumil> = withContext(Dispatchers.IO) {
        _bumilPemeriksaan.value = listOf(item) + _bumilPemeriksaan.value

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
            val resp = apiService.submitBumil(req)
            if (resp.isSuccessful) Resource.Success(item)
            else Resource.Error(statusInfo = resp.toHttpStatusInfo(), message = resp.message())
        } catch (e: Throwable) {
            Resource.Success(item)
        }
    }

    override suspend fun tambahPemeriksaanRemaja(item: PemeriksaanRemaja): Resource<PemeriksaanRemaja> = withContext(Dispatchers.IO) {
        _remajaPemeriksaan.value = listOf(item) + _remajaPemeriksaan.value

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
            val resp = apiService.submitRemaja(req)
            if (resp.isSuccessful) Resource.Success(item)
            else Resource.Error(statusInfo = resp.toHttpStatusInfo(), message = resp.message())
        } catch (e: Throwable) {
            Resource.Success(item)
        }
    }

    override suspend fun tambahPemeriksaanLansia(item: PemeriksaanLansia): Resource<PemeriksaanLansia> = withContext(Dispatchers.IO) {
        _lansiaPemeriksaan.value = listOf(item) + _lansiaPemeriksaan.value

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
            val resp = apiService.submitLansia(req)
            if (resp.isSuccessful) Resource.Success(item)
            else Resource.Error(statusInfo = resp.toHttpStatusInfo(), message = resp.message())
        } catch (e: Throwable) {
            Resource.Success(item)
        }
    }

    override fun getPemeriksaanByNik(nik: String, kategori: KategoriWarga): List<PemeriksaanItem> {
        return when (kategori) {
            KategoriWarga.BALITA -> _balitaPemeriksaan.value.filter { it.nik == nik }
            KategoriWarga.IBU_HAMIL -> _bumilPemeriksaan.value.filter { it.nik == nik }
            KategoriWarga.REMAJA -> _remajaPemeriksaan.value.filter { it.nik == nik }
            KategoriWarga.LANSIA -> _lansiaPemeriksaan.value.filter { it.nik == nik }
        }
    }

    companion object {
        private const val TAG = "PosyanduRepository"

        @Volatile
        private var instance: PosyanduRepository? = null

        fun getInstance(): PosyanduRepository {
            return instance ?: synchronized(this) {
                instance ?: PosyanduRepository().also { instance = it }
            }
        }
    }
}
