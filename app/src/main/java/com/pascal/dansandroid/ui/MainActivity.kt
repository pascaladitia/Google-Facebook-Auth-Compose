package com.pascal.dansandroid.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.pascal.dansandroid.ui.navigation.RouteScreen
import com.pascal.dansandroid.ui.theme.DansTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DansTheme(
                dynamicColor = false,
                darkTheme = false
            ) {
                RouteScreen()
            }
        }
    }
}