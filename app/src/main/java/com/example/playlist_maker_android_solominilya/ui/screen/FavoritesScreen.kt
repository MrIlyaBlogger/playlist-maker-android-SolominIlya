package com.example.playlist_maker_android_solominilya.ui.screen

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.playlist_maker_android_solominilya.R
import com.example.playlist_maker_android_solominilya.ui.navigation.TrackListItem
import com.example.playlist_maker_android_solominilya.ui.viewmodel.FavoritesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(onBackClick: () -> Unit, onTrackClick: (Long) -> Unit) {
    val darkTheme = isSystemInDarkTheme()
    val viewModel: FavoritesViewModel = viewModel()
    val favoriteList by viewModel.favoriteList.collectAsState(emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.menu_favorites)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (darkTheme) Color(0xFF1B1C20) else Color.White,
                    titleContentColor = if (darkTheme) Color.White else Color.Black,
                    navigationIconContentColor = if (darkTheme) Color.White else Color.Black
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(favoriteList) { track ->
                TrackListItem(
                    track = track,
                    onClick = { onTrackClick(track.trackId) },
                    onLongClick = { viewModel.toggleFavorite(track, false) }
                )
                HorizontalDivider(thickness = 0.5.dp)
            }
        }
    }
}
