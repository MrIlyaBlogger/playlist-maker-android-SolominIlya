package com.example.playlist_maker_android_solominilya.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.playlist_maker_android_solominilya.creator.Creator
import com.example.playlist_maker_android_solominilya.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

class PlaylistsViewModel : ViewModel() {
    private val playlistsRepository = Creator.providePlaylistsRepository()

    val playlists: Flow<List<Playlist>> = playlistsRepository.getAllPlaylists()

    suspend fun createNewPlaylist(name: String, description: String) {
        playlistsRepository.addNewPlaylist(name, description)
    }
}
