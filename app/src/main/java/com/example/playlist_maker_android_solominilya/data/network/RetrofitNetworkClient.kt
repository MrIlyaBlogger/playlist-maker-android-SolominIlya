package com.example.playlist_maker_android_solominilya.data.network

import com.example.playlist_maker_android_solominilya.creator.Storage
import com.example.playlist_maker_android_solominilya.data.dto.TracksSearchRequest
import com.example.playlist_maker_android_solominilya.data.dto.TracksSearchResponse
import com.example.playlist_maker_android_solominilya.domain.api.NetworkClient

class RetrofitNetworkClient(private val storage: Storage) : NetworkClient {

    override fun doRequest(request: Any): BaseResponse {
        val searchList = storage.search((request as TracksSearchRequest).expression)
        return TracksSearchResponse(searchList).apply { resultCode = 200 }
    }
}

