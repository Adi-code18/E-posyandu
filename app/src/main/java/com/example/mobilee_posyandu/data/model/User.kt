package com.example.mobilee_posyandu.data.model

data class User(
    val idUser: Long,
    val nama: String,
    val username: String,
    val jabatan: String
)

data class Kader(
    val idKader: Long,
    val namaKader: String,
    val username: String,
    val posko: String
)

