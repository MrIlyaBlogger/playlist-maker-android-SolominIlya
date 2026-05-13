package com.example.playlist_maker_android_solominilya.data.repository

import com.example.playlist_maker_android_solominilya.data.db.AppDatabase
import com.example.playlist_maker_android_solominilya.data.db.toDomainModel
import com.example.playlist_maker_android_solominilya.data.db.toEntity
import com.example.playlist_maker_android_solominilya.domain.api.TracksManagementRepository
import com.example.playlist_maker_android_solominilya.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class TracksManagementRepositoryImpl(private val database: AppDatabase) : TracksManagementRepository {
    private val trackDao = database.trackDao()

    override suspend fun searchTracks(expression: String): List<Track> = emptyList()

    override fun getTrackByNameAndArtist(track: Track): Flow<Track?> = flow {
        emit(trackDao.getTrackById(track.trackId)?.toDomainModel())
    }

    override fun getFavoriteTracks(): Flow<List<Track>> {
        return trackDao.getFavoriteTracks().map { list -> list.map { it.toDomainModel() } }
    }

    override suspend fun insertTrackToPlaylist(track: Track, playlistId: Long) {
        val currentTrack = trackDao.getTrackById(track.trackId)?.toDomainModel()
        trackDao.insertTrack((currentTrack ?: track).copy(playlistId = playlistId).toEntity())
    }

    override suspend fun deleteTrackFromPlaylist(track: Track) {
        val currentTrack = trackDao.getTrackById(track.trackId)?.toDomainModel()
        trackDao.insertTrack((currentTrack ?: track).copy(playlistId = 0).toEntity())
    }

    override suspend fun updateTrackFavoriteStatus(track: Track, isFavorite: Boolean) {
        val currentTrack = trackDao.getTrackById(track.trackId)?.toDomainModel()
        trackDao.insertTrack((currentTrack ?: track).copy(isFavorite = isFavorite).toEntity())
    }

    override suspend fun deleteTracksByPlaylistId(playlistId: Long) {
        // Можно реализовать позже
    }

    override suspend fun getTrackById(trackId: Long): Track? {
        return trackDao.getTrackById(trackId)?.toDomainModel()
    }

    override suspend fun insertTrack(track: Track) {
        val currentTrack = trackDao.getTrackById(track.trackId)?.toDomainModel()
        trackDao.insertTrack(
            track.copy(
                isFavorite = currentTrack?.isFavorite ?: track.isFavorite,
                playlistId = currentTrack?.playlistId ?: track.playlistId
            ).toEntity()
        )
    }

    override fun getTracksByPlaylist(playlistId: Long): Flow<List<Track>> {
        return trackDao.getTracksByPlaylist(playlistId).map { list -> list.map { it.toDomainModel() } }
    }
}
