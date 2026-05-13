package com.example.playlist_maker_android_solominilya.creator

import com.example.playlist_maker_android_solominilya.App
import com.example.playlist_maker_android_solominilya.data.db.AppDatabase
import com.example.playlist_maker_android_solominilya.data.network.RetrofitNetworkClient
import com.example.playlist_maker_android_solominilya.data.preferences.SearchHistoryStorage
import com.example.playlist_maker_android_solominilya.data.repository.PlaylistsRepositoryImpl
import com.example.playlist_maker_android_solominilya.data.repository.SearchHistoryRepositoryImpl
import com.example.playlist_maker_android_solominilya.data.repository.TracksManagementRepositoryImpl
import com.example.playlist_maker_android_solominilya.data.repository.TracksRepositoryImpl
import com.example.playlist_maker_android_solominilya.domain.api.PlaylistsRepository
import com.example.playlist_maker_android_solominilya.domain.api.SearchHistoryRepository
import com.example.playlist_maker_android_solominilya.domain.api.TracksManagementRepository
import com.example.playlist_maker_android_solominilya.domain.api.TracksRepository
import androidx.room.Room

object Creator {
    private val database: AppDatabase by lazy {
        Room.databaseBuilder(App.instance, AppDatabase::class.java, "playlist_maker.db")
            .fallbackToDestructiveMigration()   // <-- добавить
            .build()
    }

    val tracksRepository: TracksRepository by lazy {
        TracksRepositoryImpl(RetrofitNetworkClient())
    }

    private val searchHistoryStorage by lazy {
        SearchHistoryStorage(App.instance)
    }

    fun provideSearchHistoryRepository(): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(searchHistoryStorage)
    }

    fun provideTracksManagementRepository(): TracksManagementRepository {
        return TracksManagementRepositoryImpl(database)
    }

    fun providePlaylistsRepository(): PlaylistsRepository {
        return PlaylistsRepositoryImpl(database)
    }
}