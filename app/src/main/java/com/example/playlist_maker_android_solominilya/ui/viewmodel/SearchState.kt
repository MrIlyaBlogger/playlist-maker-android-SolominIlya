package com.example.playlist_maker_android_solominilya.ui.viewmodel

import com.example.playlist_maker_android_solominilya.domain.models.Track

sealed class SearchState {
    object Initial : SearchState()
    object Searching : SearchState()
    data class Success(val foundList: List<Track>) : SearchState()
    data class Fail(val error: String, val retryQuery: String) : SearchState()
}