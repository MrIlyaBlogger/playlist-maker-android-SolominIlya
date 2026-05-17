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
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Creator {
    private val migration1To2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            addCoverImagePathColumnIfNeeded(db)
        }
    }

    private val migration2To3 = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            addCoverImagePathColumnIfNeeded(db)
        }
    }

    private val database: AppDatabase by lazy {
        Room.databaseBuilder(App.instance, AppDatabase::class.java, "playlist_maker.db")
            .addMigrations(migration1To2, migration2To3)
            .build()
    }

    private fun addCoverImagePathColumnIfNeeded(db: SupportSQLiteDatabase) {
        db.query("PRAGMA table_info(playlists)").use { cursor ->
            val nameColumnIndex = cursor.getColumnIndex("name")
            var hasCoverImagePathColumn = false

            while (cursor.moveToNext()) {
                if (cursor.getString(nameColumnIndex) == "coverImagePath") {
                    hasCoverImagePathColumn = true
                    break
                }
            }

            if (!hasCoverImagePathColumn) {
                db.execSQL("ALTER TABLE playlists ADD COLUMN coverImagePath TEXT")
            }
        }
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