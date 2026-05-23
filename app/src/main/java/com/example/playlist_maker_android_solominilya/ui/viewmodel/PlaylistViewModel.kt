package com.example.playlist_maker_android_solominilya.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.playlist_maker_android_solominilya.creator.Creator
import com.example.playlist_maker_android_solominilya.domain.api.PlaylistsRepository
import com.example.playlist_maker_android_solominilya.domain.api.TracksManagementRepository
import com.example.playlist_maker_android_solominilya.domain.models.Playlist
import com.example.playlist_maker_android_solominilya.domain.models.Track
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val playlistId: Long
) : ViewModel() {
    private val playlistsRepository: PlaylistsRepository = Creator.providePlaylistsRepository()
    private val tracksManagementRepo: TracksManagementRepository = Creator.provideTracksManagementRepository()

    private val _playlist = MutableStateFlow<Playlist?>(null)
    val playlist: StateFlow<Playlist?> = _playlist.asStateFlow()

    fun updateCoverImage(coverImagePath: String?) {
        viewModelScope.launch {
            playlistsRepository.updatePlaylistCover(playlistId, coverImagePath)
        }
    }

    fun deleteTrackFromPlaylist(track: Track) {
        viewModelScope.launch {
            tracksManagementRepo.deleteTrackFromPlaylist(track)
        }
    }

    init {
        viewModelScope.launch {
            playlistsRepository.getPlaylist(playlistId)
                .combine(tracksManagementRepo.getTracksByPlaylist(playlistId)) { playlist, tracks ->
                    playlist?.copy(tracks = tracks)
                }
                .collect { playlist ->
                    _playlist.value = playlist
                }
        }
    }

    companion object {
        fun factory(playlistId: Long): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return PlaylistViewModel(playlistId) as T
                }
            }
    }
}
