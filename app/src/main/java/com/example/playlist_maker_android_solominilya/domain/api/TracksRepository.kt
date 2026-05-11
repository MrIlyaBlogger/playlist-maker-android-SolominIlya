package com.example.playlist_maker_android_solominilya.domain.api

import com.example.playlist_maker_android_solominilya.domain.models.Track

interface TracksRepository {
    suspend fun searchTracks(expression: String): List<Track>
}