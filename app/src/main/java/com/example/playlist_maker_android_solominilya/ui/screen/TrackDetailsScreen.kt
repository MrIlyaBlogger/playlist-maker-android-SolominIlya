package com.example.playlist_maker_android_solominilya.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.PlaylistAdd
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.playlist_maker_android_solominilya.R
import com.example.playlist_maker_android_solominilya.ui.viewmodel.PlaylistsViewModel
import com.example.playlist_maker_android_solominilya.ui.viewmodel.TrackDetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackDetailsScreen(
    trackId: Long,
    trackDetailsViewModel: TrackDetailsViewModel,
    playlistsViewModel: PlaylistsViewModel,
    onBackClick: () -> Unit
) {
    LaunchedEffect(trackId) {
        trackDetailsViewModel.loadTrack(trackId)
    }

    val track by trackDetailsViewModel.track.collectAsState()
    val playlists by playlistsViewModel.playlists.collectAsState(emptyList())
    var showBottomSheet by remember { mutableStateOf(false) }

    if (showBottomSheet) {
        ModalBottomSheet(onDismissRequest = { showBottomSheet = false }) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Выберите плейлист", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                if (playlists.isEmpty()) {
                    Text("Нет плейлистов")
                } else {
                    LazyColumn {
                        items(playlists) { playlist ->
                            TextButton(
                                onClick = {
                                    track?.let { trackDetailsViewModel.addToPlaylist(it, playlist.id) }
                                    showBottomSheet = false
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(playlist.name)
                            }
                        }
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Детали трека") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { innerPadding ->
        track?.let { currentTrack ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(currentTrack.artworkUrl100)
                        .crossfade(true)
                        .build(),
                    contentDescription = currentTrack.trackName,
                    modifier = Modifier.size(200.dp),
                    placeholder = painterResource(id = R.drawable.ic_music),
                    error = painterResource(id = R.drawable.ic_music)
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text(currentTrack.trackName, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text(currentTrack.artistName, fontSize = 18.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                Text(currentTrack.formattedTime, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(24.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    IconButton(onClick = {
                        trackDetailsViewModel.toggleFavorite(currentTrack, !currentTrack.isFavorite)
                    }) {
                        Icon(
                            imageVector = if (currentTrack.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Избранное",
                            tint = if (currentTrack.isFavorite) Color.Red else Color.Gray
                        )
                    }
                    IconButton(onClick = { showBottomSheet = true }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.PlaylistAdd,
                            contentDescription = "Добавить в плейлист",
                            tint = Color.Gray
                        )
                    }
                }
            }
        } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}
