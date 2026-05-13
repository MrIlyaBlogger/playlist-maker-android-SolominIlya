package com.example.playlist_maker_android_solominilya.data.db.model

data class PlaylistWithTrackCount(
    val id: Long,
    val name: String,
    val description: String,
    val trackCount: Int
)
