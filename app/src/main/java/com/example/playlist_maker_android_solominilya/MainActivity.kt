package com.example.playlist_maker_android_solominilya

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlist_maker_android_solominilya.ui.theme.PlaylistmakerandroidSolominIlyaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlaylistmakerandroidSolominIlyaTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F2F2))
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2f)
                .background(color = Color(0xFF3D6BE5))
                .padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Playlist maker",
                color = Color.White,
                fontSize = 22.sp
            )
        }

        // Menu
        Column(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = (-20).dp)
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .background(Color.White)
                .padding(top = 20.dp + 24.dp) // Padding to account for offset and spacing
        ) {
            MenuItem(icon = Icons.Default.Search, text = "Поиск") {
                Toast.makeText(context, "Нажата кнопка \"Поиск\"", Toast.LENGTH_SHORT).show()
            }
            MenuItem(icon = Icons.AutoMirrored.Filled.List, text = "Плейлисты") {
                Toast.makeText(context, "Нажата кнопка \"Плейлисты\"", Toast.LENGTH_SHORT).show()
            }
            MenuItem(icon = Icons.Default.FavoriteBorder, text = "Избранное") {
                Toast.makeText(context, "Нажата кнопка \"Избранное\"", Toast.LENGTH_SHORT).show()
            }
            MenuItem(icon = Icons.Default.Settings, text = "Настройки") {
                Toast.makeText(context, "Нажата кнопка \"Настройки\"", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Composable
fun MenuItem(icon: ImageVector, text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = Color(0xFF2A2A2A)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            color = Color(0xFF2A2A2A),
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Go",
            tint = Color(0xFFA0A0A0)
        )
    }
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun MainScreenPreview() {
    PlaylistmakerandroidSolominIlyaTheme {
        MainScreen()
    }
}
