package com.example.playlist_maker_android_solominilya.domain.api

import com.example.playlist_maker_android_solominilya.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TracksManagementRepository {
    suspend fun searchTracks(expression: String): List<Track>
    fun getTrackByNameAndArtist(track: Track): Flow<Track?>
    fun getFavoriteTracks(): Flow<List<Track>>
    suspend fun insertTrackToPlaylist(track: Track, playlistId: Long)
    suspend fun deleteTrackFromPlaylist(track: Track)
    suspend fun updateTrackFavoriteStatus(track: Track, isFavorite: Boolean)
    suspend fun deleteTracksByPlaylistId(playlistId: Long)
    suspend fun getTrackById(trackId: Long): Track?
    suspend fun insertTrack(track: Track)
    fun getTracksByPlaylist(playlistId: Long): Flow<List<Track>>   // <-- новый метод
}