package com.example.mobilee_posyandu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.mobilee_posyandu.ui.navigation.PosyanduNavHost
import com.example.mobilee_posyandu.ui.theme.BackgroundLight
import com.example.mobilee_posyandu.ui.theme.MobileEposyanduTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MobileEposyanduTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = BackgroundLight
                ) {
                    PosyanduNavHost()
                }
            }
        }
    }
}
