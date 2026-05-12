package com.example.playlist_maker_android_solominilya.domain.models

data class Track(
    val id: Long,
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val image: String = "",
    val favorite: Boolean = false,
    val playlistId: Long = 0
)