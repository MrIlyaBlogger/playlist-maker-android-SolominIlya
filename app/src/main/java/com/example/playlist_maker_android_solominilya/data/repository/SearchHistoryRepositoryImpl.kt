package com.example.playlist_maker_android_solominilya.data.repository

import com.example.playlist_maker_android_solominilya.data.DatabaseMock
import com.example.playlist_maker_android_solominilya.domain.api.SearchHistoryRepository
import com.example.playlist_maker_android_solominilya.domain.models.Word
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow

class SearchHistoryRepositoryImpl(scope: CoroutineScope) : SearchHistoryRepository {

    private val database = DatabaseMock(scope)

    override fun getHistoryRequests(): List<Word> = database.getHistoryRequests()

    override fun addToHistory(word: Word) {
        database.addToHistory(word)
    }

    override fun clearHistory() {
        database.clearHistory()
    }

    override fun getHistoryUpdates(): MutableSharedFlow<Unit> = database.getHistoryUpdates()
}