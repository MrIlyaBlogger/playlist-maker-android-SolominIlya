package com.example.playlist_maker_android_solominilya.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlist_maker_android_solominilya.data.db.dao.PlaylistDao
import com.example.playlist_maker_android_solominilya.data.db.dao.TrackDao
import com.example.playlist_maker_android_solominilya.data.db.entity.PlaylistEntity
import com.example.playlist_maker_android_solominilya.data.db.entity.TrackEntity

@Database(
    entities = [TrackEntity::class, PlaylistEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlaylistDao
}