package com.example.playlist_maker_android_solominilya.ui.navigation

enum class Screen(val route: String) {
    MAIN("main"),
    SEARCH("search"),
    SETTINGS("settings"),
    PLAYLISTS("playlists"),
    FAVORITES("favorites"),
    NEW_PLAYLIST("new_playlist"),
    TRACK_DETAILS("track_details/{trackId}"),
    PLAYLIST_DETAILS("playlist_details/{playlistId}")
}