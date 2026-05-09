package com.example.hongguolite

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.hongguolite.ui.screens.MainScreen
import com.example.hongguolite.ui.theme.HongguoLiteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HongguoLiteTheme {
                MainScreen()
            }
        }
    }
}
