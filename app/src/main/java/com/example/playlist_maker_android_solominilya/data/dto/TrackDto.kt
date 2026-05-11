package com.example.playlist_maker_android_solominilya.data.dto

data class TrackDto(
    val id: Long,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val image: String = "",
    val favorite: Boolean = false,
    val playlistId: Long = 0
)