package com.example.playlist_maker_android_solominilya.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlaylistAdd
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.playlist_maker_android_solominilya.domain.models.Playlist   // <-- важно!
import com.example.playlist_maker_android_solominilya.ui.viewmodel.PlaylistsViewModel
import com.example.playlist_maker_android_solominilya.ui.viewmodel.TrackDetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackDetailsScreen(
    trackId: Long,
    trackDetailsViewModel: TrackDetailsViewModel,
    playlistsViewModel: PlaylistsViewModel,   // <-- правильный тип
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
                                    track?.let {
                                        trackDetailsViewModel.addToPlaylist(it, playlist.id)
                                    }
                                    showBottomSheet = false
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(playlist.name)  // теперь работает, потому что импортирован Playlist
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
        track?.let { track ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(track.artworkUrl100)
                        .crossfade(true)
                        .build(),
                    contentDescription = track.trackName,
                    modifier = Modifier.size(200.dp),
                    placeholder = painterResource(id = R.drawable.ic_music),
                    error = painterResource(id = R.drawable.ic_music)
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text(track.trackName, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text(track.artistName, fontSize = 18.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                Text(track.formattedTime, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(24.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    IconButton(onClick = {
                        trackDetailsViewModel.toggleFavorite(track, !track.isFavorite)
                    }) {
                        Icon(
                            imageVector = if (track.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Избранное",
                            tint = if (track.isFavorite) Color.Red else Color.Gray
                        )
                    }
                    IconButton(onClick = { showBottomSheet = true }) {
                        Icon(
                            imageVector = Icons.Filled.PlaylistAdd,
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