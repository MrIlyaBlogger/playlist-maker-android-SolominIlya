package com.example.playlist_maker_android_solominilya.domain.models

data class Track(
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String?,
    val isFavorite: Boolean = false,
    val playlistId: Long = 0
) {
    val formattedTime: String
        get() {
            val seconds = trackTimeMillis / 1000
            val minutes = seconds / 60
            return "%02d:%02d".format(minutes, seconds % 60)
        }
}