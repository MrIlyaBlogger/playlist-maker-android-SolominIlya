package com.example.playlist_maker_android_solominilya.domain.api

import com.example.playlist_maker_android_solominilya.data.network.BaseResponse

interface NetworkClient {
    suspend fun doRequest(dto: Any): BaseResponse
}

