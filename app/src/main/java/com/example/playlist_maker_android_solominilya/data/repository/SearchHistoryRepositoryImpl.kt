package com.example.playlist_maker_android_solominilya.data.repository

import com.example.playlist_maker_android_solominilya.data.preferences.SearchHistoryStorage
import com.example.playlist_maker_android_solominilya.domain.api.SearchHistoryRepository
import com.example.playlist_maker_android_solominilya.domain.models.Word

class SearchHistoryRepositoryImpl(private val storage: SearchHistoryStorage) : SearchHistoryRepository {
    override fun getHistoryRequests(): List<Word> = storage.getEntries()

    override fun addToHistory(word: Word) {
        storage.addEntry(word.word)
    }

    override fun clearHistory() {
        storage.clear()
    }

    override fun getHistoryUpdates(): kotlinx.coroutines.flow.MutableSharedFlow<Unit> {
        // SharedFlow не используется, вместо этого будем обновлять через refreshHistory во ViewModel
        // Можно вернуть пустой, если не нужен
        return kotlinx.coroutines.flow.MutableSharedFlow()
    }
}