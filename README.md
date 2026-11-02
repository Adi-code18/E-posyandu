# e-Posyandu Mobile (Android Kotlin Jetpack Compose)

> Aplikasi Pelayanan Kesehatan Terpadu Masyarakat Berbasis Android Mobile modern dengan **Jetpack Compose (Material 3)**, **MVVM Clean Architecture**, dan **REST API Client**.

---

## 🌟 Fitur Utama
- **Autentikasi Multi-Peran**: Login cepat untuk Kader, Bidan Desa, dan Petugas Posyandu.
- **Dashboard Statistik Real-Time**: Ringkasan sasaran pelayanan (Balita, Ibu Hamil, Remaja, Lansia) dan jadwal kegiatan terdekat.
- **Manajemen Warga**: Pencarian NIK/Nama, filter kategori, registrasi warga baru 16 digit NIK.
- **Pencatatan Antropometri & Rekam Medis**: Input tumbuh kembang balita (BB, TB, LK, LA, LP, deteksi stunting), skrining kehamilan berisiko KEK, skrining anemia remaja, dan pemantauan PTM lansia.
- **Galeri Desain Status & Error Code**: Template landing page interaktif untuk penanganan respons HTTP (200, 201, 400, 401, 403, 404, 422, 500, 502, 503, Offline).
- **Dual-Mode Sync**: Sinkronisasi otomatis ke MySQL Laravel saat online, dengan graceful fallback ke cache lokal saat offline.

---

## 🚀 Memulai (Quick Start)
1. Buka folder proyek ini di **Android Studio**.
2. Biarkan Gradle melakukan sinkronisasi otomatis.
3. Hubungkan ke server Laravel backend dengan menjalankan:
   ```bash
   php artisan serve --host=0.0.0.0 --port=8000
   ```
4. Tekan **Run (Shift + F10)** pada Android Studio untuk menjalankan di Emulator atau HP Android fisik.

---

## 📖 Dokumentasi Lengkap
Panduan arsitektur, daftar endpoint, data dictionary seeder, dan dokumentasi teknis selengkapnya dapat dibaca pada:  
👉 [**`DOKUMENTASI_MOBILE.md`**](DOKUMENTASI_MOBILE.md)
