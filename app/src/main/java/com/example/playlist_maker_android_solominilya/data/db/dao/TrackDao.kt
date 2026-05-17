package com.example.playlist_maker_android_solominilya.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlist_maker_android_solominilya.data.db.entity.PlaylistTrackCrossRefEntity
import com.example.playlist_maker_android_solominilya.data.db.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlaylistTrack(crossRef: PlaylistTrackCrossRefEntity)

    @Query("SELECT * FROM tracks WHERE trackId = :trackId")
    suspend fun getTrackById(trackId: Long): TrackEntity?

    @Query("SELECT * FROM tracks WHERE is_favorite = 1")
    fun getFavoriteTracks(): Flow<List<TrackEntity>>

    @Query(
        """
        SELECT tracks.trackId, tracks.trackName, tracks.artistName, tracks.trackTimeMillis,
            tracks.artworkUrl100, tracks.is_favorite, playlist_tracks.playlist_id
        FROM tracks
        INNER JOIN playlist_tracks ON tracks.trackId = playlist_tracks.track_id
        WHERE playlist_tracks.playlist_id = :playlistId
        """
    )
    suspend fun getTracksByPlaylistList(playlistId: Long): List<TrackEntity>

    @Query(
        """
        SELECT tracks.trackId, tracks.trackName, tracks.artistName, tracks.trackTimeMillis,
            tracks.artworkUrl100, tracks.is_favorite, playlist_tracks.playlist_id
        FROM tracks
        INNER JOIN playlist_tracks ON tracks.trackId = playlist_tracks.track_id
        WHERE playlist_tracks.playlist_id = :playlistId
        """
    )
    fun getTracksByPlaylist(playlistId: Long): Flow<List<TrackEntity>>

    @Query("DELETE FROM tracks WHERE trackId = :trackId")
    suspend fun deleteTrackById(trackId: Long)

    @Query("DELETE FROM playlist_tracks WHERE track_id = :trackId AND playlist_id = :playlistId")
    suspend fun deleteTrackFromPlaylist(trackId: Long, playlistId: Long)

    @Query("DELETE FROM playlist_tracks WHERE playlist_id = :playlistId")
    suspend fun deletePlaylistTracksByPlaylistId(playlistId: Long)

    @Query(
        """
        DELETE FROM tracks
        WHERE is_favorite = 0
            AND trackId NOT IN (SELECT track_id FROM playlist_tracks)
        """
    )
    suspend fun deleteTracksWithoutReferences()
}
