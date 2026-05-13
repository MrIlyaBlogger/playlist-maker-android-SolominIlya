package com.example.playlist_maker_android_solominilya.data.preferences

import android.content.Context
import com.example.playlist_maker_android_solominilya.domain.models.Word

class SearchHistoryStorage(context: Context) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun addEntry(word: String) {
        if (word.isBlank()) return
        val history = getEntries().map { it.word }.toMutableList()
        history.remove(word)
        history.add(0, word)
        if (history.size > MAX_ENTRIES) {
            history.subList(0, MAX_ENTRIES)
        }
        val serialized = history.joinToString(SEPARATOR)
        prefs.edit().putString(KEY_HISTORY, serialized).apply()
    }

    fun getEntries(): List<Word> {
        val raw = prefs.getString(KEY_HISTORY, "") ?: ""
        if (raw.isEmpty()) return emptyList()
        return raw.split(SEPARATOR).map { Word(it) }
    }

    fun clear() {
        prefs.edit().remove(KEY_HISTORY).apply()
    }

    companion object {
        private const val PREFS_NAME = "search_history"
        private const val KEY_HISTORY = "history"
        private const val MAX_ENTRIES = 10
        private const val SEPARATOR = ","
    }
}