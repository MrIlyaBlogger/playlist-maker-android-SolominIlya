package com.example.playlist_maker_android_solominilya.data

import com.example.playlist_maker_android_solominilya.domain.models.Playlist
import com.example.playlist_maker_android_solominilya.domain.models.Track
import com.example.playlist_maker_android_solominilya.domain.models.Word
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class DatabaseMock(private val scope: CoroutineScope) {

    private val historyList = mutableListOf<Word>()
    private val _historyUpdates = MutableSharedFlow<Unit>()
    private val playlists = mutableListOf<Playlist>()
    private val tracks = mutableListOf<Track>()

    // --- История ---
    fun getHistoryRequests(): List<Word> = historyList.toList()

    fun addToHistory(word: Word) {
        historyList.add(word)
        notifyHistoryChanged()
    }

    fun clearHistory() {
        historyList.clear()
        notifyHistoryChanged()
    }

    fun getHistoryUpdates(): MutableSharedFlow<Unit> = _historyUpdates

    private fun notifyHistoryChanged() {
        scope.launch(Dispatchers.IO) {
            _historyUpdates.emit(Unit)
        }
    }

    // --- Плейлисты ---
    fun getAllPlaylists(): Flow<List<Playlist>> = flow {
        delay(500)
        val filteredPlaylists = playlists.map { playlist ->
            val playlistTracks = tracks.filter { it.playlistId == playlist.id }
            playlist.copy(tracks = playlistTracks)
        }
        emit(filteredPlaylists)
    }

    fun getPlaylist(id: Long): Flow<Playlist?> = flow {
        val playlist = playlists.find { it.id == id }
        if (playlist != null) {
            val playlistTracks = tracks.filter { it.playlistId == id }
            emit(playlist.copy(tracks = playlistTracks))
        } else {
            emit(null)
        }
    }

    fun addNewPlaylist(name: String, description: String) {
        playlists.add(
            Playlist(
                id = playlists.size.toLong() + 1,
                name = name,
                description = description,
                tracks = emptyList()
            )
        )
    }

    fun deletePlaylistById(playlistId: Long) {
        playlists.removeIf { it.id == playlistId }
    }

    fun deleteTracksByPlaylistId(playlistId: Long) {
        tracks.removeIf { it.playlistId == playlistId }
    }

    // --- Треки ---
    fun getTrackById(trackId: Long): Track? = tracks.find { it.trackId == trackId }
    fun getTrackByNameAndArtist(track: Track): Flow<Track?> = flow {
        emit(tracks.find { it.trackName == track.trackName && it.artistName == track.artistName })
    }

    fun insertTrack(track: Track) {
        tracks.removeIf { it.trackId == track.trackId }
        tracks.add(track)
    }

    fun getFavoriteTracks(): Flow<List<Track>> = flow {
        delay(300)
        emit(tracks.filter { it.isFavorite })
    }

    fun deleteTrackFromPlaylist(trackId: Long) {
        tracks.removeIf { it.trackId == trackId }
    }

    fun searchTracks(expression: String): List<Track> {
        return tracks.filter { it.trackName.contains(expression, ignoreCase = true) }
    }
}