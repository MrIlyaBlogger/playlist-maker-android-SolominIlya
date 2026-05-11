package com.example.playlist_maker_android_solominilya.data

import com.example.playlist_maker_android_solominilya.domain.models.Word
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class DatabaseMock(val scope: CoroutineScope) {

    private val historyList = mutableListOf<Word>()
    private val _historyUpdates = MutableSharedFlow<Unit>()

    fun getHistoryRequests(): List<Word> {
        return historyList.toList()
    }

    fun getHistoryUpdates(): MutableSharedFlow<Unit> = _historyUpdates

    fun addToHistory(word: Word) {
        historyList.add(word)
        notifyHistoryChanged()
    }

    fun clearHistory() {
        historyList.clear()
        notifyHistoryChanged()
    }

    private fun notifyHistoryChanged() {
        scope.launch(Dispatchers.IO) {
            _historyUpdates.emit(Unit)
        }
    }
}