package com.example.playlist_maker_android_solominilya.data.db

import com.example.playlist_maker_android_solominilya.data.db.entity.PlaylistEntity
import com.example.playlist_maker_android_solominilya.data.db.entity.TrackEntity
import com.example.playlist_maker_android_solominilya.domain.models.Playlist
import com.example.playlist_maker_android_solominilya.domain.models.Track

fun TrackEntity.toDomainModel(): Track {
    return Track(
        trackId = this.trackId,
        trackName = this.trackName,
        artistName = this.artistName,
        trackTimeMillis = this.trackTimeMillis,
        artworkUrl100 = this.artworkUrl100,
        isFavorite = this.isFavorite,
        playlistId = this.playlistId
    )
}

fun Track.toEntity(): TrackEntity {
    return TrackEntity(
        trackId = this.trackId,
        trackName = this.trackName,
        artistName = this.artistName,
        trackTimeMillis = this.trackTimeMillis,
        artworkUrl100 = this.artworkUrl100,
        isFavorite = this.isFavorite,
        playlistId = this.playlistId
    )
}

fun PlaylistEntity.toDomainModel(tracks: List<Track> = emptyList()): Playlist {
    return Playlist(
        id = this.id,
        name = this.name,
        description = this.description,
        coverImagePath = this.coverImagePath,
        tracks = tracks
    )
}

fun Playlist.toEntity(): PlaylistEntity {
    return PlaylistEntity(
        id = this.id,
        name = this.name,
        description = this.description,
        coverImagePath = this.coverImagePath
    )
}
