package com.example.playlist_maker_android_solominilya.data.repository

import com.example.playlist_maker_android_solominilya.data.DatabaseMock
import com.example.playlist_maker_android_solominilya.domain.api.TracksManagementRepository
import com.example.playlist_maker_android_solominilya.domain.models.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

class TracksManagementRepositoryImpl(
    private val scope: CoroutineScope,
    private val database: DatabaseMock
) : TracksManagementRepository {

    override suspend fun searchTracks(expression: String): List<Track> =
        database.searchTracks(expression)

    override fun getTrackByNameAndArtist(track: Track): Flow<Track?> =
        database.getTrackByNameAndArtist(track)

    override fun getFavoriteTracks(): Flow<List<Track>> =
        database.getFavoriteTracks()

    override suspend fun insertTrackToPlaylist(track: Track, playlistId: Long) {
        database.insertTrack(track.copy(playlistId = playlistId))
    }

    override suspend fun deleteTrackFromPlaylist(track: Track) {
        database.insertTrack(track.copy(playlistId = 0))
    }

    override suspend fun updateTrackFavoriteStatus(track: Track, isFavorite: Boolean) {
        database.insertTrack(track.copy(isFavorite = isFavorite))
    }

    override suspend fun deleteTracksByPlaylistId(playlistId: Long) {
        database.deleteTracksByPlaylistId(playlistId)
    }

    override suspend fun getTrackById(trackId: Long): Track? =
        database.getTrackById(trackId)

    override suspend fun insertTrack(track: Track) {
        database.insertTrack(track)
    }
}