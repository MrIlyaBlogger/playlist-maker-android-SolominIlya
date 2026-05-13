package com.example.playlist_maker_android_solominilya.data.dto

import com.google.gson.annotations.SerializedName

data class TrackDto(
    @SerializedName("trackId") val trackId: Long = 0,
    @SerializedName("id") val id: Long = 0,
    val trackName: String? = null,
    val artistName: String? = null,
    val trackTimeMillis: Long? = null,
    @SerializedName("artworkUrl100") val artworkUrl100: String? = null
)