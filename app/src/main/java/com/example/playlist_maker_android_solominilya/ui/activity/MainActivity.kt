package com.example.playlist_maker_android_solominilya.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.playlist_maker_android_solominilya.ui.navigation.PlaylistHost
import com.example.playlist_maker_android_solominilya.ui.theme.PlaylistmakerandroidSolominIlyaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlaylistmakerandroidSolominIlyaTheme {
                val navController = rememberNavController()
                PlaylistHost(navController = navController)
            }
        }
    }
}
