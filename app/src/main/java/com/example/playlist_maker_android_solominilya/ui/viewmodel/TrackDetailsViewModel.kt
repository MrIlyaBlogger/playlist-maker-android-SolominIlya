package com.example.playlist_maker_android_solominilya.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist_maker_android_solominilya.creator.Creator
import com.example.playlist_maker_android_solominilya.domain.api.TracksManagementRepository
import com.example.playlist_maker_android_solominilya.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TrackDetailsViewModel : ViewModel() {
    private val tracksRepo: TracksManagementRepository = Creator.provideTracksManagementRepository(viewModelScope)

    private val _track = MutableStateFlow<Track?>(null)
    val track: StateFlow<Track?> = _track.asStateFlow()

    fun loadTrack(trackId: Long) {
        viewModelScope.launch {
            _track.value = tracksRepo.getTrackById(trackId)
        }
    }

    fun toggleFavorite(track: Track, isFavorite: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            tracksRepo.updateTrackFavoriteStatus(track, isFavorite)
            _track.value = _track.value?.copy(isFavorite = isFavorite)
        }
    }

    fun addToPlaylist(track: Track, playlistId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            tracksRepo.insertTrackToPlaylist(track, playlistId)
        }
    }
}