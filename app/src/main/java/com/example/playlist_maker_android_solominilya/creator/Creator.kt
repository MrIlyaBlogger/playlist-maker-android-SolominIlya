package com.example.playlist_maker_android_solominilya.creator

import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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

object Creator {
    private val migration1To2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            normalizePlaylistsTable(db)
        }
    }

    private val migration2To3 = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            normalizePlaylistsTable(db)
        }
    }

    private val migration3To4 = object : Migration(3, 4) {
        override fun migrate(db: SupportSQLiteDatabase) {
            normalizePlaylistsTable(db)
        }
    }

    private val database: AppDatabase by lazy {
        Room.databaseBuilder(App.instance, AppDatabase::class.java, "playlist_maker.db")
            .addMigrations(migration1To2, migration2To3, migration3To4)
            .build()
    }

    private fun normalizePlaylistsTable(db: SupportSQLiteDatabase) {
        if (!tableExists(db, PLAYLISTS_TABLE)) {
            createPlaylistsTable(db, PLAYLISTS_TABLE)
            return
        }

        val playlistColumns = getColumnNames(db, PLAYLISTS_TABLE)
        val hasExpectedColumns = playlistColumns.containsAll(EXPECTED_PLAYLIST_COLUMNS)
        val hasUnexpectedColumns = playlistColumns.any { it !in EXPECTED_PLAYLIST_COLUMNS }

        if (hasExpectedColumns && !hasUnexpectedColumns) {
            return
        }

        db.execSQL("DROP TABLE IF EXISTS $PLAYLISTS_TEMP_TABLE")
        createPlaylistsTable(db, PLAYLISTS_TEMP_TABLE)
        copyPlaylistsToNormalizedTable(db, playlistColumns)
        db.execSQL("DROP TABLE $PLAYLISTS_TABLE")
        db.execSQL("ALTER TABLE $PLAYLISTS_TEMP_TABLE RENAME TO $PLAYLISTS_TABLE")
    }

    private fun tableExists(db: SupportSQLiteDatabase, tableName: String): Boolean {
        db.query("SELECT name FROM sqlite_master WHERE type = 'table' AND name = ?", arrayOf(tableName))
            .use { cursor ->
                return cursor.moveToFirst()
            }
    }

    private fun getColumnNames(db: SupportSQLiteDatabase, tableName: String): Set<String> {
        db.query("PRAGMA table_info($tableName)").use { cursor ->
            val nameColumnIndex = cursor.getColumnIndex("name")
            val columns = mutableSetOf<String>()

            while (cursor.moveToNext()) {
                columns.add(cursor.getString(nameColumnIndex))
            }

            return columns
        }
    }

    private fun createPlaylistsTable(db: SupportSQLiteDatabase, tableName: String) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS $tableName (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                name TEXT NOT NULL,
                description TEXT NOT NULL,
                coverImagePath TEXT
            )
            """.trimIndent()
        )
    }

    private fun copyPlaylistsToNormalizedTable(db: SupportSQLiteDatabase, columns: Set<String>) {
        val idExpression = if ("id" in columns) "id" else "0"
        val nameExpression = if ("name" in columns) "name" else "''"
        val descriptionExpression = if ("description" in columns) "description" else "''"
        val coverImagePathExpression = when {
            "coverImagePath" in columns && "coverImageUri" in columns -> "COALESCE(coverImagePath, coverImageUri)"
            "coverImagePath" in columns -> "coverImagePath"
            "coverImageUri" in columns -> "coverImageUri"
            else -> "NULL"
        }

        db.execSQL(
            """
            INSERT INTO $PLAYLISTS_TEMP_TABLE (id, name, description, coverImagePath)
            SELECT $idExpression, $nameExpression, $descriptionExpression, $coverImagePathExpression
            FROM $PLAYLISTS_TABLE
            """.trimIndent()
        )
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

    private const val PLAYLISTS_TABLE = "playlists"
    private const val PLAYLISTS_TEMP_TABLE = "playlists_new"

    private val EXPECTED_PLAYLIST_COLUMNS = setOf(
        "id",
        "name",
        "description",
        "coverImagePath"
    )
}
