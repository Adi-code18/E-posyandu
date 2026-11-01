package com.example.mobilee_posyandu.data.model

object MockData {
    val users = listOf(
        User(1, "Dr. Hendra Kusuma", "ketua", "Ketua Posyandu"),
        User(2, "Administrator Posyandu", "admin", "Administrator Sistem"),
        User(3, "Bdn. Ratna Juwita, S.Tr.Keb", "bidan_desa", "Bidan Desa Pembina"),
        User(4, "Siti Nurhaliza, A.Md.Gz", "nutrisionis", "Petugas Gizi Puskesmas")
    )

    val kaders = listOf(
        Kader(1, "Ibu Kader Anggrek", "kader1", "Pos Anggrek (Utama)"),
        Kader(2, "Ibu Sri Wahyuni", "kader_mawar", "Pos Mawar"),
        Kader(3, "Ibu Siti Rahayu", "kader_melati", "Pos Melati"),
        Kader(4, "Ibu Endang Lestari", "kader_dahlia", "Pos Dahlia")
    )

    val wargaList = mutableListOf(
        // Balita
        Warga(
            nik = "3201020304050001",
            namaLengkap = "Aditya Pratama",
            tanggalLahir = "12 April 2023",
            jenisKelamin = "Laki-laki",
            namaAyah = "Rudi Hartono",
            namaIbu = "Siti Aminah",
            status = KategoriWarga.BALITA,
            alamat = "Dusun Anggrek RT 01 / RW 02"
        ),
        Warga(
            nik = "3274012023080004",
            namaLengkap = "Arlo Wijaya",
            tanggalLahir = "15 Agustus 2023",
            jenisKelamin = "Laki-laki",
            namaAyah = "Budi Wijaya",
            namaIbu = "Rina Marlina",
            status = KategoriWarga.BALITA,
            alamat = "Dusun Mawar RT 03 / RW 01"
        ),
        Warga(
            nik = "3201021405230002",
            namaLengkap = "Naura Kayla Az-Zahra",
            tanggalLahir = "20 Januari 2024",
            jenisKelamin = "Perempuan",
            namaAyah = "Agus Salim",
            namaIbu = "Maya Safitri",
            status = KategoriWarga.BALITA,
            alamat = "Dusun Dahlia RT 02 / RW 03"
        ),
        // Ibu Hamil
        Warga(
            nik = "3201024508920005",
            namaLengkap = "Siti Aminah",
            tanggalLahir = "20 Agustus 1995",
            jenisKelamin = "Perempuan",
            namaAyah = "-",
            namaIbu = "-",
            status = KategoriWarga.IBU_HAMIL,
            alamat = "Dusun Anggrek RT 01 / RW 02"
        ),
        Warga(
            nik = "3201025203970008",
            namaLengkap = "Dewi Lestari Kusuma",
            tanggalLahir = "24 Maret 1997",
            jenisKelamin = "Perempuan",
            namaAyah = "-",
            namaIbu = "-",
            status = KategoriWarga.IBU_HAMIL,
            alamat = "Dusun Melati RT 02 / RW 01"
        ),
        // Remaja
        Warga(
            nik = "3201021804080003",
            namaLengkap = "Dimas Arya Pratama",
            tanggalLahir = "18 April 2008",
            jenisKelamin = "Laki-laki",
            namaAyah = "Hartono",
            namaIbu = "Rini Wulandari",
            status = KategoriWarga.REMAJA,
            alamat = "Dusun Mawar RT 01 / RW 01"
        ),
        Warga(
            nik = "3201025509090006",
            namaLengkap = "Putri Maharani",
            tanggalLahir = "15 September 2009",
            jenisKelamin = "Perempuan",
            namaAyah = "Slamet Riyadi",
            namaIbu = "Indah Permata",
            status = KategoriWarga.REMAJA,
            alamat = "Dusun Anggrek RT 03 / RW 02"
        ),
        // Lansia
        Warga(
            nik = "3201021203550002",
            namaLengkap = "Karto Wijoyo",
            tanggalLahir = "12 Maret 1955",
            jenisKelamin = "Laki-laki",
            namaAyah = "-",
            namaIbu = "-",
            status = KategoriWarga.LANSIA,
            alamat = "Dusun Melati RT 01 / RW 01"
        ),
        Warga(
            nik = "3201024807530007",
            namaLengkap = "Hj. Sutinah",
            tanggalLahir = "18 Juli 1953",
            jenisKelamin = "Perempuan",
            namaAyah = "-",
            namaIbu = "-",
            status = KategoriWarga.LANSIA,
            alamat = "Dusun Dahlia RT 04 / RW 03"
        )
    )

    val pemeriksaanBalitaList = mutableListOf(
        PemeriksaanBalita(
            idPeriksa = 1,
            nik = "3201020304050001",
            tanggal = "2026-10-15",
            bb = 10.5,
            tb = 82.0,
            lk = 46.5,
            la = 14.5,
            lp = 48.0,
            status = "Gizi Baik (Normal)"
        ),
        PemeriksaanBalita(
            idPeriksa = 2,
            nik = "3201020304050001",
            tanggal = "2026-09-15",
            bb = 10.1,
            tb = 80.5,
            lk = 46.0,
            la = 14.2,
            lp = 47.5,
            status = "Gizi Baik (Normal)"
        ),
        PemeriksaanBalita(
            idPeriksa = 3,
            nik = "3274012023080004",
            tanggal = "2026-10-15",
            bb = 8.2,
            tb = 73.0,
            lk = 44.0,
            la = 12.8,
            lp = 43.0,
            status = "Risiko Stunting"
        ),
        PemeriksaanBalita(
            idPeriksa = 4,
            nik = "3201021405230002",
            tanggal = "2026-10-15",
            bb = 7.8,
            tb = 68.5,
            lk = 43.0,
            la = 13.5,
            lp = 42.0,
            status = "Gizi Baik (Normal)"
        )
    )

    val pemeriksaanBumilList = mutableListOf(
        PemeriksaanBumil(
            idPeriksa = 101,
            nik = "3201024508920005",
            tanggal = "2026-10-10",
            usiaKehamilan = "Trimester 2 (24 Minggu)",
            bb = 61.5,
            tb = 158.0,
            la = 24.5,
            lp = 88.0,
            tensi = "115/75",
            status = "Normal Sehat"
        ),
        PemeriksaanBumil(
            idPeriksa = 102,
            nik = "3201025203970008",
            tanggal = "2026-10-12",
            usiaKehamilan = "Trimester 1 (11 Minggu)",
            bb = 47.0,
            tb = 152.0,
            la = 22.0,
            lp = 74.0,
            tensi = "105/70",
            status = "Risiko KEK (Kurang Energi Kronis)"
        )
    )

    val pemeriksaanRemajaList = mutableListOf(
        PemeriksaanRemaja(
            idPeriksa = 201,
            nik = "3201021804080003",
            tanggal = "2026-10-08",
            bb = 52.0,
            tb = 165.0,
            tensi = "110/70",
            hemoglobin = 13.5,
            gula = 95.0,
            status = "Normal"
        ),
        PemeriksaanRemaja(
            idPeriksa = 202,
            nik = "3201025509090006",
            tanggal = "2026-10-08",
            bb = 44.0,
            tb = 154.0,
            tensi = "100/65",
            hemoglobin = 10.8,
            gula = 88.0,
            status = "Anemia Ringan (Perlu TTD)"
        )
    )

    val pemeriksaanLansiaList = mutableListOf(
        PemeriksaanLansia(
            idPeriksa = 301,
            nik = "3201021203550002",
            tanggal = "2026-10-05",
            bb = 64.0,
            tb = 162.0,
            tensi = "140/90",
            gula = 145.0,
            riwayatPenyakit = "Hipertensi Derajat 1",
            status = "Terkontrol (Edukasi Diet Rendah Garam)"
        ),
        PemeriksaanLansia(
            idPeriksa = 302,
            nik = "3201024807530007",
            tanggal = "2026-10-05",
            bb = 56.0,
            tb = 150.0,
            tensi = "125/80",
            gula = 110.0,
            riwayatPenyakit = "Asam Urat Ringan",
            status = "Kondisi Baik"
        )
    )

    val jadwalList = mutableListOf(
        JadwalPosyandu(
            idJadwal = 1,
            tanggalPelaksanaan = "05 November 2026",
            kegiatanPelaksanaan = "Penimbangan Balita, Distribusi Vitamin A & PMT",
            statusPelaksanaan = "belum",
            lokasi = "Posyandu Anggrek RW 02"
        ),
        JadwalPosyandu(
            idJadwal = 2,
            tanggalPelaksanaan = "10 November 2026",
            kegiatanPelaksanaan = "Pemeriksaan Rutin Ibu Hamil (ANC) & USG Dasar Bidan Desa",
            statusPelaksanaan = "belum",
            lokasi = "Balai Desa / Puskesmas Pembantu"
        ),
        JadwalPosyandu(
            idJadwal = 3,
            tanggalPelaksanaan = "15 November 2026",
            kegiatanPelaksanaan = "Posbindu PTM Lansia: Skrining Tensi, Gula Darah & Senam Sehat",
            statusPelaksanaan = "belum",
            lokasi = "Posyandu Melati RW 01"
        ),
        JadwalPosyandu(
            idJadwal = 4,
            tanggalPelaksanaan = "20 November 2026",
            kegiatanPelaksanaan = "Posyandu Remaja: Skrining Hb, Penyuluhan Gizi & Pembagian TTD",
            statusPelaksanaan = "belum",
            lokasi = "Gedung Serbaguna Desa"
        )
    )

    val informasiList = listOf(
        InformasiUmum(
            idInformasi = 1,
            judul = "Pentingnya 1000 Hari Pertama Kehidupan (HPK)",
            isiInformasi = "Periode 1000 HPK adalah masa emas pertumbuhan anak sejak dalam kandungan hingga usia 2 tahun. Nutrisi yang optimal pada masa ini mencegah stunting permanen.",
            tanggalPost = "28 Okt 2026",
            kategoriInfo = "Nutrisi"
        ),
        InformasiUmum(
            idInformasi = 2,
            judul = "Panduan MPASI Bergizi Kaya Protein Hewani",
            isiInformasi = "Telur, ikan kembung, ayam, dan daging sangat esensial untuk perkembangan otak balita. Berikan secara rutin pada menu makanan pendamping ASI.",
            tanggalPost = "25 Okt 2026",
            kategoriInfo = "Tumbuh Kembang"
        ),
        InformasiUmum(
            idInformasi = 3,
            judul = "Waspadai Anemia pada Remaja Putri",
            isiInformasi = "Remaja putri dianjurkan mengonsumsi 1 Tablet Tambah Darah (TTD) setiap minggu selama menstruasi untuk menjaga konsentrasi belajar dan mencegah anemia.",
            tanggalPost = "20 Okt 2026",
            kategoriInfo = "Nutrisi"
        ),
        InformasiUmum(
            idInformasi = 4,
            judul = "Tips Menjaga Tekanan Darah dan Gula bagi Lansia",
            isiInformasi = "Kurangi konsumsi garam harian, perbanyak jalan santai pagi minimal 20 menit, dan rutin periksa tensi di Posyandu Lansia setiap bulan.",
            tanggalPost = "15 Okt 2026",
            kategoriInfo = "Lansia"
        )
    )
}

