package com.pwani.majisafi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.pwani.majisafi.ui.screens.BoreholeScreen
import com.pwani.majisafi.ui.theme.MajiSafiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MajiSafiTheme {
                BoreholeScreen()
            }
        }
    }
}
