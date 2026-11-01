package com.example.mobilee_posyandu.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobilee_posyandu.data.model.KategoriWarga

@Composable
fun CategoryBadge(
    kategori: KategoriWarga,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = kategori.badgeColor.copy(alpha = 0.15f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = kategori.label,
            color = kategori.badgeColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

