package com.example.playlist_maker_android_solominilya.creator

import com.example.playlist_maker_android_solominilya.data.network.RetrofitNetworkClient
import com.example.playlist_maker_android_solominilya.data.repository.TracksRepositoryImpl
import com.example.playlist_maker_android_solominilya.domain.api.TracksRepository

object Creator {
    private val storage by lazy { Storage() }
    private val networkClient by lazy { RetrofitNetworkClient(storage) }

    val tracksRepository: TracksRepository by lazy { TracksRepositoryImpl(networkClient) }
}