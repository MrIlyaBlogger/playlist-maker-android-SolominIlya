package com.example.playlist_maker_android_solominilya.domain.api

import com.example.playlist_maker_android_solominilya.domain.models.Word

interface SearchHistoryRepository {
    fun getHistoryRequests(): List<Word>
    fun addToHistory(word: Word)
    fun clearHistory()
    fun getHistoryUpdates(): kotlinx.coroutines.flow.MutableSharedFlow<Unit>
}