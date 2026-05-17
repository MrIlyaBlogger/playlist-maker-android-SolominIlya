package com.example.playlist_maker_android_solominilya.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlist_maker_android_solominilya.data.db.entity.PlaylistEntity
import com.example.playlist_maker_android_solominilya.data.db.model.PlaylistWithTrackCount
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlists")
    fun getAllPlaylists(): Flow<List<PlaylistEntity>>

    @Query(
        """
        SELECT playlists.id, playlists.name, playlists.description, playlists.coverImagePath, COUNT(tracks.trackId) as trackCount
        FROM playlists
        LEFT JOIN tracks ON playlists.id = tracks.playlist_id
        GROUP BY playlists.id, playlists.name, playlists.description, playlists.coverImagePath
        """
    )
    fun getAllPlaylistsWithTrackCount(): Flow<List<PlaylistWithTrackCount>>

    @Query("SELECT * FROM playlists WHERE id = :playlistId")
    fun getPlaylistById(playlistId: Long): Flow<PlaylistEntity?>

    @Query("DELETE FROM playlists WHERE id = :playlistId")
    suspend fun deletePlaylistById(playlistId: Long)
}
