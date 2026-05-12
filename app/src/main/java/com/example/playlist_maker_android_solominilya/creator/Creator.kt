package com.example.playlist_maker_android_solominilya.creator

import com.example.playlist_maker_android_solominilya.data.DatabaseMock
import com.example.playlist_maker_android_solominilya.data.network.RetrofitNetworkClient
import com.example.playlist_maker_android_solominilya.data.repository.PlaylistsRepositoryImpl
import com.example.playlist_maker_android_solominilya.data.repository.SearchHistoryRepositoryImpl
import com.example.playlist_maker_android_solominilya.data.repository.TracksManagementRepositoryImpl
import com.example.playlist_maker_android_solominilya.data.repository.TracksRepositoryImpl
import com.example.playlist_maker_android_solominilya.domain.api.PlaylistsRepository
import com.example.playlist_maker_android_solominilya.domain.api.SearchHistoryRepository
import com.example.playlist_maker_android_solominilya.domain.api.TracksManagementRepository
import com.example.playlist_maker_android_solominilya.domain.api.TracksRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

object Creator {
    // Единая база данных для плейлистов и избранного
    private val databaseMock = DatabaseMock(CoroutineScope(Dispatchers.IO))

    // Поисковый репозиторий – использует сетевой клиент
    val tracksRepository: TracksRepository by lazy {
        TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideSearchHistoryRepository(scope: CoroutineScope): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(scope)
    }

    fun provideTracksManagementRepository(scope: CoroutineScope): TracksManagementRepository {
        return TracksManagementRepositoryImpl(scope, databaseMock)
    }

    fun providePlaylistsRepository(scope: CoroutineScope): PlaylistsRepository {
        return PlaylistsRepositoryImpl(scope, databaseMock)
    }
}