package com.example.mobilee_posyandu.data.model

import androidx.compose.ui.graphics.Color
import com.example.mobilee_posyandu.ui.theme.BalitaColor
import com.example.mobilee_posyandu.ui.theme.BumilColor
import com.example.mobilee_posyandu.ui.theme.LansiaColor
import com.example.mobilee_posyandu.ui.theme.RemajaColor

enum class KategoriWarga(val label: String, val badgeColor: Color) {
    BALITA("Balita", BalitaColor),
    IBU_HAMIL("Ibu Hamil", BumilColor),
    REMAJA("Remaja", RemajaColor),
    LANSIA("Lansia", LansiaColor);

    companion object {
        fun fromString(value: String): KategoriWarga {
            return when (value.trim().lowercase()) {
                "balita" -> BALITA
                "ibu hamil", "bumil" -> IBU_HAMIL
                "remaja" -> REMAJA
                "lansia" -> LANSIA
                else -> BALITA
            }
        }
    }
}

