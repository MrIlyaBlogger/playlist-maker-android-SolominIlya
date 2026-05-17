package com.example.playlist_maker_android_solominilya.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "playlist_tracks",
    primaryKeys = ["playlist_id", "track_id"]
)
data class PlaylistTrackCrossRefEntity(
    @ColumnInfo(name = "playlist_id")
    val playlistId: Long,
    @ColumnInfo(name = "track_id")
    val trackId: Long
)
