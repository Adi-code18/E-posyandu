# DOKUMENTASI LENGKAP APLIKASI MOBILE e-POSYANDU
## Kotlin Jetpack Compose • Clean Architecture & Best Practices

> **Aplikasi Pelayanan Kesehatan Terpadu Masyarakat Berbasis Mobile Android**  
> Dibuat menggunakan **Kotlin**, **Jetpack Compose (Material 3)**, **MVVM Architecture**, dan terintegrasi dengan **Laravel 11 REST API**.

---

## DAFTAR ISI
1. [Tentang Aplikasi & Arsitektur](#1-tentang-aplikasi--arsitektur)
2. [Spesifikasi Teknologi](#2-spesifikasi-teknologi)
3. [Struktur Direktori Proyek](#3-struktur-direktori-proyek)
4. [Penerapan Best Practices Arsitektur (MVVM & UDF)](#4-penerapan-best-practices-arsitektur-mvvm--udf)
5. [Desain & Penanganan HTTP Status / Error Code](#5-desain--penanganan-http-status--error-code)
6. [Integrasi Jaringan & REST API Laravel](#6-integrasi-jaringan--rest-api-laravel)
7. [Daftar Endpoint API & Pemetaan di Mobile](#7-daftar-endpoint-api--pemetaan-di-mobile)
8. [Akun Pengujian & Data Demo Default](#8-akun-pengujian--data-demo-default)
9. [Panduan Instalasi & Menjalankan Proyek](#9-panduan-instalasi--menjalankan-proyek)

---

## 1. Tentang Aplikasi & Arsitektur

Aplikasi **e-Posyandu Mobile** dirancang sebagai antarmuka lapangan bagi Kader Posyandu, Bidan Desa, dan Petugas Kesehatan untuk melakukan pencatatan dan pemantauan siklus hidup kesehatan secara cepat, akurat, dan ramah pengguna (*mobile-first*).

### Arsitektur yang Diterapkan:
Aplikasi mengadopsi **Clean Architecture** dengan pola **MVVM (Model-View-ViewModel)** dan **Unidirectional Data Flow (UDF)** yang direkomendasikan secara resmi oleh Google Android:

```
┌────────────────────────────────────────────────────────────────────────┐
│                        ALUR DATA (UDF & MVVM)                          │
├────────────────────────────────────────────────────────────────────────┤
│                                                                        │
│   [ UI (Composable Screen) ] ── (Events / User Action) ──► [ ViewModel ]
│                ▲                                                 │     │
│                │                                                 │     │
│       (StateFlow / UiState)                            (Coroutine/IO)  │
│                │                                                 ▼     │
│       [ UI State Binding ] ◄── (Resource<T> State) ──── [ Repository ] │
│                                                                  │     │
│                                                   ┌──────────────┴───┐ │
│                                                   ▼                  ▼ │
│                                            [ Retrofit API ]   [ Local Cache ]
│                                            (Laravel Server)   (Offline Mode)│
└────────────────────────────────────────────────────────────────────────┘
```

1. **Unidirectional Data Flow**: State mengalir ke bawah (dari ViewModel ke UI Composable), sementara Event/Aksi pengguna mengalir ke atas (dari UI ke ViewModel).
2. **Stateless UI**: Komponen tampilan (Composable) hanya fokus merender tampilan berdasarkan State yang diterimanya, sehingga mudah diuji dan mendukung `@Preview` di Android Studio.
3. **Repository Pattern dengan Interface (`IPosyanduRepository`)**: Mengabstraksi sumber data (apakah dari remote API Laravel atau mock data lokal), menerapkan prinsip *Dependency Inversion*.
4. **Dual-Mode (Online & Offline Graceful Fallback)**: Jika backend Laravel aktif, data disinkronkan secara langsung ke database MySQL. Jika offline / server lokal mati, aplikasi tetap berfungsi tanpa *force close* menggunakan cache lokal.

---

## 2. Spesifikasi Teknologi

| Komponen | Spesifikasi / Pustaka |
| :--- | :--- |
| **Bahasa Pemrograman** | Kotlin 2.2+ |
| **UI Toolkit** | Jetpack Compose (BOM 2026.02.01) |
| **Design System** | Material 3 (Theme, Elevation, Dynamic Typography) |
| **Arsitektur** | MVVM, Package-by-Feature, Clean Architecture |
| **Manajemen State** | Kotlin Coroutines, `StateFlow`, `MutableStateFlow` |
| **Navigasi** | Navigation Compose 2.8.8 (Single-Activity NavHost) |
| **Klien HTTP / REST API**| Retrofit 2.11.0 + OkHttp 4.12.0 (Logging Interceptor) |
| **Serialization / JSON** | Google Gson Converter |
| **Target SDK / Min SDK** | Target SDK 37, Min SDK 24 (Android 7.0 Nougat+) |

---

## 3. Struktur Direktori Proyek

Proyek ditata menggunakan pendekatan **Package-by-Feature** agar setiap modul modular, rapi, dan mudah dirawat:

```plaintext
app/src/main/java/com/example/mobilee_posyandu/
├── data/
│   ├── model/                     # Data Class Entitas Domain
│   │   ├── User.kt                # Model Akun Petugas & Kader
│   │   ├── Warga.kt               # Model Biodata Kependudukan
│   │   ├── KategoriWarga.kt       # Enum: BALITA, IBU_HAMIL, REMAJA, LANSIA
│   │   ├── Pemeriksaan.kt         # Model Medis Antropometri 4 Kelompok Usia
│   │   ├── JadwalPosyandu.kt      # Model Agenda Kegiatan Posyandu
│   │   ├── InformasiUmum.kt       # Model Edukasi & Artikel Kesehatan
│   │   └── MockData.kt            # Seeder Data Offline (Identik Database Laravel)
│   ├── remote/                    # Jaringan & REST API
│   │   ├── ApiConfig.kt           # Konfigurasi Base URL & OkHttpClient
│   │   ├── PosyanduApiService.kt  # Definisi 22 Endpoint Retrofit
│   │   ├── ApiResponse.kt         # Wrapper Standar { success, message, data, errors }
│   │   └── Dto.kt                 # Data Transfer Object (Request & Response)
│   ├── repository/                # Lapisan Repositori Data
│   │   ├── IPosyanduRepository.kt # Interface Abstraksi Data (Best Practice)
│   │   └── PosyanduRepository.kt  # Implementasi Sinkronisasi Real-Time + Cache
│   └── util/                      # Utilitas & Helper
│       └── Resource.kt            # Wrapper State (Success, Error, Loading, Idle)
│
├── ui/
│   ├── components/                # Komponen UI Reusable
│   │   ├── PosyanduTopBar.kt      # App Bar Konsisten & Tombol Aksi
│   │   ├── CategoryBadge.kt       # Pill Warna Dinamis per Kelompok Usia
│   │   ├── StatCard.kt            # Kartu Statistik Sasaran Pelayanan
│   │   ├── WargaItemCard.kt       # Kartu Warga dengan Avatar Inisial & Gender
│   │   ├── HttpStatusInfo.kt      # Enum Spesifikasi 11 HTTP Status/Error Code
│   │   └── HttpStatusLandingView.kt # Desain Landing Page Error/Status Code
│   ├── navigation/
│   │   ├── Screen.kt              # Daftar Rute Navigasi Aplikasi
│   │   └── PosyanduNavHost.kt     # Pengendali Alur Navigasi (Single NavHost)
│   ├── theme/
│   │   ├── Color.kt               # Palet Warna Kesehatan (Teal, Sky, Pink, Lansia)
│   │   ├── Theme.kt               # Konfigurasi Tema Material 3
│   │   └── Type.kt                # Tipografi Typography Material 3
│   └── features/                  # Modul Layar per Fitur (Package-by-Feature)
│       ├── auth/
│       │   ├── LoginScreen.kt     # Halaman Masuk Akun
│       │   └── AuthViewModel.kt   # Logika Bisnis & Validasi Autentikasi
│       ├── dashboard/
│       │   ├── DashboardScreen.kt # Beranda Ringkasan Statistik & Jadwal
│       │   └── DashboardViewModel.kt # State Aggregator Beranda
│       ├── warga/
│       │   ├── WargaListScreen.kt # Cari Nama/NIK & Filter Kategori
│       │   ├── WargaDetailScreen.kt # Profil Warga & Riwayat Antropometri (404 Handled)
│       │   ├── TambahWargaScreen.kt # Form Registrasi Warga Baru (NIK 16 Digit)
│       │   └── WargaViewModel.kt  # Logika Pencarian, Filter & Registrasi
│       ├── pemeriksaan/
│       │   ├── CatatPemeriksaanScreen.kt # Form Dinamis Sesuai Kelompok Sasaran
│       │   └── PemeriksaanViewModel.kt   # Logika Pencatatan Rekam Medis Fisik
│       ├── status/
│       │   └── StatusCodeShowcaseScreen.kt # Galeri Interaktif 11 HTTP Status Code
│       ├── jadwal/
│       │   └── JadwalScreen.kt    # Kalender Agenda Posyandu
│       └── edukasi/
│           └── EdukasiScreen.kt   # Artikel Gizi, 1000 HPK & Pencegahan Stunting
│
└── MainActivity.kt                # Entry Point Tunggal Pembungkus Tema & NavHost
```

---

## 4. Penerapan Best Practices Arsitektur (MVVM & UDF)

Proyek ini telah direfaktor untuk menerapkan standar industri Android:

### 1. Pemisahan State dan Logika (ViewModel)
- Komponen UI tidak melakukan pemanggilan API atau mutasi data secara langsung.
- Setiap layar fitur didukung oleh `ViewModel` khusus (`AuthViewModel`, `DashboardViewModel`, `WargaViewModel`, `PemeriksaanViewModel`).
- State didefinisikan dalam data class yang immutable (contoh: `DashboardUiState`, `WargaListUiState`).

### 2. Penanganan Error yang Tangguh (`Resource<T>`)
- Seluruh panggilan API dibungkus dalam `Resource.Success`, `Resource.Error`, atau `Resource.Loading`.
- Status kode HTTP dari Retrofit secara otomatis dipetakan ke dalam `HttpStatusCodeInfo` melalui ekstensi `Response<T>.toHttpStatusInfo()`.
- Kendala jaringan (`ConnectException`, `SocketTimeoutException`) otomatis dipetakan ke `HttpStatusCodeInfo.NETWORK_OFFLINE`.

### 3. State Hoisting & Stateless Composables
- Parameter Composable menerima `viewModel` dengan nilai default `viewModel()`.
- Hal ini memungkinkan layar Composable dipratinjau (*Preview*) di Android Studio atau diuji dengan mock ViewModel secara mudah.

---

## 5. Desain & Penanganan HTTP Status / Error Code

Aplikasi memiliki sistem penanganan status kode yang dirancang secara visual melalui [`HttpStatusLandingView.kt`](file:///c:/Users/Adi%20Darmawan/AndroidStudioProjects/mobileeposyandu/app/src/main/java/com/example/mobilee_posyandu/ui/components/HttpStatusLandingView.kt) dan katalog di [`StatusCodeShowcaseScreen.kt`](file:///c:/Users/Adi%20Darmawan/AndroidStudioProjects/mobileeposyandu/app/src/main/java/com/example/mobilee_posyandu/ui/features/status/StatusCodeShowcaseScreen.kt):

| Kode | Kategori | Judul Halaman | Arti & Contoh Kasus di e-Posyandu | Aksi Penanganan |
| :--- | :--- | :--- | :--- | :--- |
| **200** | 2xx Sukses | **Permintaan Berhasil** | Data balita, profil warga, atau jadwal sukses dimuat dari server. | Lanjutkan aktivitas |
| **201** | 2xx Sukses | **Data Baru Berhasil Dibuat** | Warga baru atau hasil penimbangan balita sukses disimpan ke database. | Lihat data tersimpan |
| **400** | 4xx Klien | **Format Data Tidak Sesuai** | Parameter input tidak valid (misal: berat badan diisi teks). | Periksa isian form |
| **401** | 4xx Klien | **Sesi Anda Telah Berakhir** | Token login kedaluwarsa atau belum masuk sistem. | Masuk ulang akun |
| **403** | 4xx Klien | **Akses Menu Dibatasi** | Kader biasa mencoba membuka modul khusus pimpinan/Bidan. | Kembali ke dashboard |
| **404** | 4xx Klien | **Halaman Tidak Ditemukan** | NIK tidak terdaftar di database atau rute halaman salah. *(Otomatis tampil jika NIK tidak ditemukan di Detail Warga)* | Cari data lain |
| **422** | 4xx Klien | **Validasi Formulir Gagal** | NIK kurang dari 16 digit, NIK duplikat, atau kolom wajib belum diisi. | Perbaiki formulir |
| **500** | 5xx Server | **Gangguan Internal Server** | Database MySQL mati atau terjadi *runtime error* pada backend Laravel. | Muat ulang / Lapor |
| **502** | 5xx Server | **Gerbang Server Bermasalah** | Web server proxy gagal meneruskan koneksi ke PHP service. | Coba hubungkan ulang |
| **503** | 5xx Server | **Layanan Pemeliharaan** | Sistem sedang dalam mode maintenance (`php artisan down`). | Cek status berkala |
| **OFFLINE** | Jaringan | **Koneksi Terputus** | HP tidak ada koneksi atau `php artisan serve` belum aktif. | Buka mode lokal |

---

## 6. Integrasi Jaringan & REST API Laravel

### 1. Izin Jaringan di `AndroidManifest.xml`
```xml
<uses-permission android:name="android.permission.INTERNET" />

<application
    android:usesCleartextTraffic="true"
    ...>
```
`usesCleartextTraffic="true"` mengizinkan aplikasi berkomunikasi melalui protokol HTTP lokal tanpa terblokir keamanan SSL.

### 2. Konfigurasi Base URL ([`ApiConfig.kt`](file:///c:/Users/Adi%20Darmawan/AndroidStudioProjects/mobileeposyandu/app/src/main/java/com/example/mobilee_posyandu/data/remote/ApiConfig.kt))
- **Jika Menjalankan di Android Studio Emulator**:
  Emulator menggunakan alamat IP virtual khusus `10.0.2.2` untuk mengakses `localhost` laptop host:
  ```kotlin
  var baseUrl: String = "http://10.0.2.2:8000/api/"
  ```
- **Jika Menjalankan di HP Fisik (USB Debugging / Wi-Fi)**:
  Cek IP lokal laptop Anda melalui `ipconfig` (misal `192.168.1.15`), lalu ubah URL:
  ```kotlin
  ApiConfig.setCustomBaseUrl("http://192.168.1.15:8000/api/")
  ```

### 3. Menjalankan Server Laravel
Agar server dapat dijangkau oleh perangkat luar maupun emulator, jalankan dengan host `0.0.0.0`:
```bash
cd c:\laragon\www\e-Posyandu\e-Posyandu
php artisan serve --host=0.0.0.0 --port=8000
```

---

## 7. Daftar Endpoint API & Pemetaan di Mobile

Seluruh rute pada [`routes/api.php`](file:///c:/laragon/www/e-Posyandu/e-Posyandu/routes/api.php) telah dipetakan ke dalam metode Retrofit pada [`PosyanduApiService.kt`](file:///c:/Users/Adi%20Darmawan/AndroidStudioProjects/mobileeposyandu/app/src/main/java/com/example/mobilee_posyandu/data/remote/PosyanduApiService.kt):

| No | Modul | HTTP Method | Endpoint Laravel | Metode Retrofit di Android |
| :--- | :--- | :--- | :--- | :--- |
| 1 | Autentikasi | `POST` | `/api/auth/login` | `login(body: LoginRequest)` |
| 2 | Autentikasi | `POST` | `/api/auth/logout` | `logout()` |
| 3 | Dashboard | `GET` | `/api/dashboard` | `getDashboard()` |
| 4 | Warga | `GET` | `/api/warga` | `getWarga(search, status, page, perPage)` |
| 5 | Warga | `POST` | `/api/warga` | `createWarga(body: WargaStoreRequest)` |
| 6 | Warga | `GET` | `/api/warga/{nik}` | `getWargaDetail(nik)` |
| 7 | Balita | `POST` | `/api/pemeriksaan/balita` | `submitBalita(body: PemeriksaanBalitaRequest)` |
| 8 | Balita | `GET` | `/api/pemeriksaan/balita` | `getHistoryBalita()` |
| 9 | Ibu Hamil | `POST` | `/api/pemeriksaan/bumil` | `submitBumil(body: PemeriksaanBumilRequest)` |
| 10 | Ibu Hamil | `GET` | `/api/pemeriksaan/bumil` | `getHistoryBumil()` |
| 11 | Remaja | `POST` | `/api/pemeriksaan/remaja` | `submitRemaja(body: PemeriksaanRemajaRequest)` |
| 12 | Remaja | `GET` | `/api/pemeriksaan/remaja` | `getHistoryRemaja()` |
| 13 | Lansia | `POST` | `/api/pemeriksaan/lansia` | `submitLansia(body: PemeriksaanLansiaRequest)` |
| 14 | Lansia | `GET` | `/api/pemeriksaan/lansia` | `getHistoryLansia()` |
| 15 | Jadwal | `GET` | `/api/jadwal` | `getJadwal()` |
| 16 | Artikel | `GET` | `/api/artikel` | `getArtikel()` |

---

## 8. Akun Pengujian & Data Demo Default

Untuk kemudahan pengujian (*testing*), sistem seeder dan mock lokal menyediakan data yang sama:

### Akun Petugas & Kader:
- **Kader Posyandu**: Username `kader1` / Password `password`
- **Bidan Desa**: Username `bidan_desa` / Password `password`
- **Ketua Posyandu**: Username `ketua` / Password `password`
*(Pada layar [LoginScreen.kt](file:///c:/Users/Adi%20Darmawan/AndroidStudioProjects/mobileeposyandu/app/src/main/java/com/example/mobilee_posyandu/ui/features/auth/LoginScreen.kt), Anda cukup mengetuk chip pilihan peran untuk otomatis mengisi kredensial).*

### Contoh NIK Warga untuk Pencarian:
- **Balita**: `3201020304050001` (Aditya Pratama), `3274012023080004` (Arlo Wijaya - Risiko Stunting).
- **Ibu Hamil**: `3201024508920005` (Siti Aminah), `3201025203970008` (Dewi Lestari Kusuma - KEK).
- **Remaja**: `3201021804080003` (Dimas Arya Pratama), `3201025509090006` (Putri Maharani - Anemia).
- **Lansia**: `3201021203550002` (Karto Wijoyo - Hipertensi), `3201024807530007` (Hj. Sutinah).

---

## 9. Panduan Instalasi & Menjalankan Proyek

1. **Buka Project di Android Studio**:
   - Jalankan Android Studio, pilih **Open**, arahkan ke:  
     `c:\Users\Adi Darmawan\AndroidStudioProjects\mobileeposyandu`.
2. **Sinkronisasi Gradle**:
   - Tunggu proses Gradle Sync selesai secara otomatis.
3. **Jalankan Aplikasi**:
   - Pilih perangkat target (Android Emulator atau HP Fisik via USB Debugging).
   - Tekan tombol **Run (Shift + F10)**.
4. **Menguji Status Code**:
   - Di Beranda/Dashboard, ketuk kartu banner biru **`🎨 Desain Status & Error Code`** untuk membuka galeri interaktif seluruh status code.

