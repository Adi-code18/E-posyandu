package com.example.mobilee_posyandu.data.remote

import retrofit2.Response
import retrofit2.http.*

interface PosyanduApiService {

    // --- 1. Autentikasi ---
    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): Response<ApiResponse<LoginResponseData>>

    @POST("auth/logout")
    suspend fun logout(): Response<ApiResponse<Unit>>

    // --- 2. Dashboard ---
    @GET("dashboard")
    suspend fun getDashboard(): Response<ApiResponse<DashboardResponseData>>

    // --- 3. Warga Sasaran ---
    @GET("warga")
    suspend fun getWarga(
        @Query("search") search: String? = null,
        @Query("status") status: String? = null,
        @Query("page") page: Int? = null,
        @Query("per_page") perPage: Int? = 50
    ): Response<ApiResponse<PaginationResponse<WargaDto>>>

    @POST("warga")
    suspend fun createWarga(@Body body: WargaStoreRequest): Response<ApiResponse<WargaDto>>

    @GET("warga/{nik}")
    suspend fun getWargaDetail(@Path("nik") nik: String): Response<ApiResponse<WargaDto>>

    // --- 4. Pemeriksaan Antropometri ---
    @POST("pemeriksaan/balita")
    suspend fun submitBalita(@Body body: PemeriksaanBalitaRequest): Response<ApiResponse<PemeriksaanBalitaDto>>

    @GET("pemeriksaan/balita")
    suspend fun getHistoryBalita(): Response<ApiResponse<List<PemeriksaanBalitaDto>>>

    @POST("pemeriksaan/bumil")
    suspend fun submitBumil(@Body body: PemeriksaanBumilRequest): Response<ApiResponse<PemeriksaanBumilDto>>

    @GET("pemeriksaan/bumil")
    suspend fun getHistoryBumil(): Response<ApiResponse<List<PemeriksaanBumilDto>>>

    @POST("pemeriksaan/remaja")
    suspend fun submitRemaja(@Body body: PemeriksaanRemajaRequest): Response<ApiResponse<PemeriksaanRemajaDto>>

    @GET("pemeriksaan/remaja")
    suspend fun getHistoryRemaja(): Response<ApiResponse<List<PemeriksaanRemajaDto>>>

    @POST("pemeriksaan/lansia")
    suspend fun submitLansia(@Body body: PemeriksaanLansiaRequest): Response<ApiResponse<PemeriksaanLansiaDto>>

    @GET("pemeriksaan/lansia")
    suspend fun getHistoryLansia(): Response<ApiResponse<List<PemeriksaanLansiaDto>>>

    // --- 5. Jadwal Posyandu ---
    @GET("jadwal")
    suspend fun getJadwal(): Response<ApiResponse<List<JadwalDto>>>

    // --- 6. Artikel & Edukasi ---
    @GET("artikel")
    suspend fun getArtikel(): Response<ApiResponse<List<ArtikelDto>>>
}

