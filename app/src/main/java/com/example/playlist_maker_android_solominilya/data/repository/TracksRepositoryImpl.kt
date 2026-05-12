package com.example.playlist_maker_android_solominilya.data.repository

import com.example.playlist_maker_android_solominilya.data.dto.TracksSearchRequest
import com.example.playlist_maker_android_solominilya.data.dto.TracksSearchResponse
import com.example.playlist_maker_android_solominilya.domain.api.NetworkClient
import com.example.playlist_maker_android_solominilya.domain.api.TracksRepository
import com.example.playlist_maker_android_solominilya.domain.models.Track

class TracksRepositoryImpl(
    private val networkClient: NetworkClient
) : TracksRepository {

    override suspend fun searchTracks(expression: String): List<Track> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        if (response.resultCode == 200 && response is TracksSearchResponse) {
            return response.results.map { dto ->
                Track(
                    trackId = dto.trackId,
                    trackName = dto.trackName ?: "Неизвестно",
                    artistName = dto.artistName ?: "Неизвестный исполнитель",
                    trackTimeMillis = dto.trackTimeMillis ?: 0L,
                    artworkUrl100 = dto.artworkUrl100
                )
            }
        } else {
            throw Exception(response.errorMessage ?: "Ошибка сети")
        }
    }
}