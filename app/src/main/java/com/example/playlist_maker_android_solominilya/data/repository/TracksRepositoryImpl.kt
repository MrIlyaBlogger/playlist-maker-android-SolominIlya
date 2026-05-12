package com.example.playlist_maker_android_solominilya.data.repository

import com.example.playlist_maker_android_solominilya.data.dto.TracksSearchRequest
import com.example.playlist_maker_android_solominilya.data.dto.TracksSearchResponse
import com.example.playlist_maker_android_solominilya.domain.api.NetworkClient
import com.example.playlist_maker_android_solominilya.domain.api.TracksRepository
import com.example.playlist_maker_android_solominilya.domain.models.Track
import kotlinx.coroutines.delay

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {

    override suspend fun searchTracks(expression: String): List<Track> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        delay(1000) // эмулируем задержку
        return if (response.resultCode == 200) {
            (response as TracksSearchResponse).results.map {
                val seconds = it.trackTimeMillis / 1000
                val minutes = seconds / 60
                val trackTime = "%02d:%02d".format(minutes, seconds % 60)
                Track(
                    id = it.id,
                    trackName = it.trackName,
                    artistName = it.artistName,
                    trackTime = trackTime,
                    image = it.image,
                    favorite = it.favorite,
                    playlistId = it.playlistId
                )
            }
        } else {
            emptyList()
        }
    }
}