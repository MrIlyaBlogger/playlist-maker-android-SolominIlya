package com.example.playlist_maker_android_solominilya.data.repository

import com.example.playlist_maker_android_solominilya.data.DatabaseMock
import com.example.playlist_maker_android_solominilya.domain.api.PlaylistsRepository
import com.example.playlist_maker_android_solominilya.domain.models.Playlist
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

class PlaylistsRepositoryImpl(
    private val scope: CoroutineScope,
    private val database: DatabaseMock
) : PlaylistsRepository {

    override fun getPlaylist(playlistId: Long): Flow<Playlist?> = database.getPlaylist(playlistId)

    override fun getAllPlaylists(): Flow<List<Playlist>> = database.getAllPlaylists()

    override suspend fun addNewPlaylist(name: String, description: String) {
        database.addNewPlaylist(name, description)
    }

    override suspend fun deletePlaylistById(id: Long) {
        database.deletePlaylistById(id)
    }
}