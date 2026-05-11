package com.example.playlist_maker_android_solominilya.creator

import com.example.playlist_maker_android_solominilya.data.dto.TrackDto

class Storage {
    private val listTracks = listOf(
        TrackDto(1, "Владивосток 2000", "Мумий Троль", 158000, "", false, 0),
        TrackDto(2, "Группа крови", "Кино", 283000, "", false, 0),
        TrackDto(3, "Не смотри назад", "Ария", 312000, "", false, 0),
        TrackDto(4, "Звезда по имени Солнце", "Кино", 225000, "", false, 0),
        TrackDto(5, "Лондон", "Аквариум", 272000, "", false, 0),
        TrackDto(6, "На заре", "Альянс", 230000, "", false, 0),
        TrackDto(7, "Перемен", "Кино", 296000, "", false, 0),
        TrackDto(8, "Розовый фламинго", "Сплин", 195000, "", false, 0),
        TrackDto(9, "Танцевать", "Мельница", 222000, "", false, 0),
        TrackDto(10, "Чёрный бумер", "Серега", 241000, "", false, 0)
    )

    fun search(request: String): List<TrackDto> {
        val query = request.lowercase()
        return listTracks.filter { track ->
            track.trackName.lowercase().contains(query)
        }
    }
}