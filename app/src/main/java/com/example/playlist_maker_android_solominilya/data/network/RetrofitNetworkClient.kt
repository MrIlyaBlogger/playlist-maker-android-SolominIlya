package com.example.playlist_maker_android_solominilya.data.network

import com.example.playlist_maker_android_solominilya.data.dto.TracksSearchRequest
import com.example.playlist_maker_android_solominilya.domain.api.NetworkClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class RetrofitNetworkClient : NetworkClient {

    private val api: ITunesApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApiService::class.java)
    }

    override suspend fun doRequest(dto: Any): BaseResponse {
        return try {
            when (dto) {
                is TracksSearchRequest -> {
                    val response = api.searchTracks(query = dto.expression)
                    response.apply { resultCode = 200 }
                }
                else -> BaseResponse().apply {
                    resultCode = 400
                    errorMessage = "Invalid request type"
                }
            }
        } catch (e: IOException) {
            BaseResponse().apply {
                resultCode = -1
                errorMessage = "Network error: ${e.message ?: "Unknown IO error"}"
            }
        } catch (e: Exception) {
            BaseResponse().apply {
                resultCode = -2
                errorMessage = "Unexpected error: ${e.message ?: "Unknown error"}"
            }
        }
    }
}