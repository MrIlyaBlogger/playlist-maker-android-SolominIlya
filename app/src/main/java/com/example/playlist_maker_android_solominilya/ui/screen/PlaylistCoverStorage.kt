package com.example.playlist_maker_android_solominilya.ui.screen

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID

suspend fun copyPlaylistCoverToInternalStorage(context: Context, sourceUri: Uri): String? {
    return withContext(Dispatchers.IO) {
        runCatching {
            val coversDirectory = File(context.filesDir, "playlist_covers")
            if (!coversDirectory.exists()) {
                coversDirectory.mkdirs()
            }
            val coverFile = File(coversDirectory, "${UUID.randomUUID()}.jpg")
            context.contentResolver.openInputStream(sourceUri)?.use { inputStream ->
                coverFile.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            } ?: return@runCatching null
            coverFile.toUri().toString()
        }.getOrNull()
    }
}
