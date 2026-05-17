package com.example.playlist_maker_android_solominilya.data.repository

import com.example.playlist_maker_android_solominilya.data.db.AppDatabase
import com.example.playlist_maker_android_solominilya.data.db.toDomainModel
import com.example.playlist_maker_android_solominilya.data.db.toEntity
import com.example.playlist_maker_android_solominilya.domain.api.PlaylistsRepository
import com.example.playlist_maker_android_solominilya.domain.models.Playlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistsRepositoryImpl(private val database: AppDatabase) : PlaylistsRepository {
    private val playlistDao = database.playlistDao()
    private val trackDao = database.trackDao()

    override fun getPlaylist(playlistId: Long): Flow<Playlist?> {
        return playlistDao.getPlaylistById(playlistId).map { entity ->
            entity?.let {
                // Для деталей плейлиста треки загружаются отдельно через PlaylistViewModel
                // Здесь возвращаем плейлист без треков, чтобы избежать сложной логики
                it.toDomainModel(emptyList())
            }
        }
    }

    override fun getAllPlaylists(): Flow<List<Playlist>> {
        return playlistDao.getAllPlaylistsWithTrackCount().map { entities ->
            entities.map { entity ->
                Playlist(
                    id = entity.id,
                    name = entity.name,
                    description = entity.description,
                    coverImagePath = entity.coverImagePath,
                    trackCount = entity.trackCount,
                    tracks = emptyList()
                )
            }
        }
    }

    override suspend fun addNewPlaylist(name: String, description: String, coverImagePath: String?) {
        playlistDao.insertPlaylist(
            Playlist(
                id = 0,
                name = name,
                description = description,
                coverImagePath = coverImagePath
            ).toEntity()
        )
    }

    override suspend fun deletePlaylistById(id: Long) {
        playlistDao.deletePlaylistById(id)
    }
}
