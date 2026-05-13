package com.example.playlist_maker_android_solominilya.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.playlist_maker_android_solominilya.creator.Creator
import com.example.playlist_maker_android_solominilya.domain.api.TracksManagementRepository
import com.example.playlist_maker_android_solominilya.domain.api.SearchHistoryRepository
import com.example.playlist_maker_android_solominilya.domain.api.TracksRepository
import com.example.playlist_maker_android_solominilya.domain.models.Word
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

class SearchViewModel(
    private val tracksRepository: TracksRepository
) : ViewModel() {

    private val searchHistoryRepository: SearchHistoryRepository =
        Creator.provideSearchHistoryRepository()

    private val tracksManagementRepo: TracksManagementRepository =
        Creator.provideTracksManagementRepository()

    private val _searchQuery = MutableStateFlow("")
    private val _searchScreenState = MutableStateFlow<SearchState>(SearchState.Initial)
    val searchScreenState: StateFlow<SearchState> = _searchScreenState.asStateFlow()

    private val _historyState = MutableStateFlow<List<Word>>(emptyList())
    val historyState: StateFlow<List<Word>> = _historyState.asStateFlow()

    init {
        refreshHistory()
        viewModelScope.launch {
            _searchQuery
                .debounce(1000)
                .distinctUntilChanged()
                .collect { query ->
                    if (query.isNotEmpty()) {
                        search(query)
                    }
                }
        }
    }

    fun updateQuery(query: String) {
        _searchQuery.value = query
    }

    fun search(whatSearch: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _searchScreenState.update { SearchState.Searching }
                searchHistoryRepository.addToHistory(Word(word = whatSearch))
                refreshHistory()
                val list = tracksRepository.searchTracks(whatSearch)
                // Сохраняем каждый трек в общую базу Room
                list.forEach { track ->
                    tracksManagementRepo.insertTrack(track)
                }
                _searchScreenState.update { SearchState.Success(foundList = list) }
            } catch (e: IOException) {
                _searchScreenState.update { SearchState.Fail(e.message ?: "Ошибка сети", whatSearch) }
            } catch (e: Exception) {
                _searchScreenState.update { SearchState.Fail(e.message ?: "Ошибка", whatSearch) }
            }
        }
    }

    fun clearSearch() {
        _searchQuery.value = ""
        _searchScreenState.update { SearchState.Initial }
        refreshHistory()
    }

    fun refreshHistory() {
        _historyState.value = searchHistoryRepository.getHistoryRequests()
    }

    fun clearHistory() {
        searchHistoryRepository.clearHistory()
        refreshHistory()
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SearchViewModel(Creator.tracksRepository) as T
                }
            }
    }
}