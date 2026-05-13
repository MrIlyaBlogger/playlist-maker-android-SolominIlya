package com.example.playlist_maker_android_solominilya.domain.models

data class Playlist(
    val id: Long = 0,
    val name: String,
    val description: String,
    val trackCount: Int = 0,
    val tracks: List<Track> = emptyList()
)
