package com.example.playlist_maker_android_solominilya

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Headset
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.example.playlist_maker_android_solominilya.ui.theme.PlaylistmakerandroidSolominIlyaTheme

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlaylistmakerandroidSolominIlyaTheme {
                SettingsScreen(onBackClick = { finish() })
            }
        }
    }
}

@SuppressLint("UseKtx")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onBackClick: () -> Unit) {
    val darkTheme = isSystemInDarkTheme()
    val context = LocalContext.current

    val shareMessage = stringResource(id = R.string.share_message)
    val supportSubject = stringResource(id = R.string.support_email_subject)
    val supportBody = stringResource(id = R.string.support_email_body)
    val agreementUrl = stringResource(id = R.string.user_agreement_url)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(id = R.string.settings_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = if (darkTheme) Color(0xFF1B1C20) else Color.White,
                    titleContentColor = if (darkTheme) Color.White else Color.Black,
                    navigationIconContentColor = if (darkTheme) Color.White else Color.Black
                )
            )
        },
        containerColor = if (darkTheme) Color(0xFF1B1C20) else Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            SettingsItem(
                title = stringResource(id = R.string.dark_theme),
                icon = null,
                isSwitchVisible = true
            )
            SettingsItem(
                title = stringResource(id = R.string.share_app),
                icon = Icons.Default.Share
            ) {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, shareMessage)
                }
                context.startActivity(Intent.createChooser(intent, null))
            }
            SettingsItem(
                title = stringResource(id = R.string.write_to_support),
                icon = Icons.Outlined.Headset
            ) {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = "mailto:".toUri()
                    putExtra(Intent.EXTRA_EMAIL, arrayOf("mr.ilyablogger@ya.ru"))
                    putExtra(Intent.EXTRA_SUBJECT, supportSubject)
                    putExtra(Intent.EXTRA_TEXT, supportBody)
                }
                context.startActivity(intent)
            }
            SettingsItem(
                title = stringResource(id = R.string.user_agreement),
                icon = Icons.AutoMirrored.Filled.KeyboardArrowRight
            ) {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = agreementUrl.toUri()
                }
                context.startActivity(intent)
            }
        }
    }
}

@Composable
fun SettingsItem(
    title: String,
    icon: ImageVector?,
    isSwitchVisible: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    val darkTheme = isSystemInDarkTheme()
    var switchState by remember { mutableStateOf(darkTheme) }
    val textColor = if (darkTheme) Color(0xFFE7E7E7) else Color(0xFF1E1E1E)
    val iconColor = if (darkTheme) Color(0xFF8A8A8A) else Color(0xFF707070)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable(enabled = onClick != null, onClick = { onClick?.invoke() })
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = title, color = textColor)
        if (isSwitchVisible) {
            Switch(
                checked = switchState,
                onCheckedChange = { switchState = it }, // No-op, as requested
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color(0xFF3D6BE5),
                    uncheckedThumbColor = if (darkTheme) Color(0xFF717171) else Color(0xFFC0C0C0),
                    uncheckedTrackColor = if (darkTheme) Color(0xFF2B2B2F) else Color(0xFFE3E3E3)
                )
            )
        } else if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconColor
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    PlaylistmakerandroidSolominIlyaTheme {
        SettingsScreen(onBackClick = {})
    }
}
