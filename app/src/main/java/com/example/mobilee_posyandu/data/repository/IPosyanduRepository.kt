package com.example.mobilee_posyandu.data.repository

import com.example.mobilee_posyandu.data.model.*
import com.example.mobilee_posyandu.data.util.Resource
import kotlinx.coroutines.flow.StateFlow

/**
 * Interface Repository utama e-Posyandu.
 * Mengikuti prinsip Dependency Inversion (Clean Architecture).
 */
interface IPosyanduRepository {
    val currentUser: StateFlow<String?>
    val authToken: StateFlow<String?>
    val isOnline: StateFlow<Boolean>

    val wargaList: StateFlow<List<Warga>>
    val balitaPemeriksaan: StateFlow<List<PemeriksaanBalita>>
    val bumilPemeriksaan: StateFlow<List<PemeriksaanBumil>>
    val remajaPemeriksaan: StateFlow<List<PemeriksaanRemaja>>
    val lansiaPemeriksaan: StateFlow<List<PemeriksaanLansia>>
    val jadwalList: StateFlow<List<JadwalPosyandu>>
    val informasiList: StateFlow<List<InformasiUmum>>

    suspend fun refreshFromApi(): Resource<Unit>
    suspend fun login(username: String, role: String, password: String = "password"): Resource<String>
    suspend fun logout(): Resource<Unit>

    fun getWargaByNik(nik: String): Warga?
    suspend fun tambahWarga(warga: Warga): Resource<Warga>

    suspend fun tambahPemeriksaanBalita(item: PemeriksaanBalita): Resource<PemeriksaanBalita>
    suspend fun tambahPemeriksaanBumil(item: PemeriksaanBumil): Resource<PemeriksaanBumil>
    suspend fun tambahPemeriksaanRemaja(item: PemeriksaanRemaja): Resource<PemeriksaanRemaja>
    suspend fun tambahPemeriksaanLansia(item: PemeriksaanLansia): Resource<PemeriksaanLansia>

    fun getPemeriksaanByNik(nik: String, kategori: KategoriWarga): List<PemeriksaanItem>
}

