package com.example.playlist_maker_android_solominilya.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist_maker_android_solominilya.creator.Creator
import com.example.playlist_maker_android_solominilya.domain.api.TracksManagementRepository
import com.example.playlist_maker_android_solominilya.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoritesViewModel : ViewModel() {
    private val tracksRepo: TracksManagementRepository = Creator.provideTracksManagementRepository()

    val favoriteList: StateFlow<List<Track>> = tracksRepo.getFavoriteTracks().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun toggleFavorite(track: Track, isFavorite: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            tracksRepo.updateTrackFavoriteStatus(track, isFavorite)
        }
    }
}
