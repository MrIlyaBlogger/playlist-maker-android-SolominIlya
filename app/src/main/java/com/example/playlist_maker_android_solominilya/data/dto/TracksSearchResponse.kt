package com.example.playlist_maker_android_solominilya.data.dto

import com.example.playlist_maker_android_solominilya.data.network.BaseResponse

data class TracksSearchResponse(
    val resultCount: Int,
    val results: List<TrackDto>
) : BaseResponse()