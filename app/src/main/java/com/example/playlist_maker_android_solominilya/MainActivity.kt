package com.example.playlist_maker_android_solominilya
import com.example.playlist_maker_android_solominilya.ui.navigation.PlaylistHost

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
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
