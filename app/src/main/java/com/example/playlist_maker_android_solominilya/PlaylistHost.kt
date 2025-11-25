package com.example.playlist_maker_android_solominilya

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Headset
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun PlaylistHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.MAIN.route) {
        composable(Screen.MAIN.route) {
            MainScreen(
                onNavigateToSearch = { navController.navigate(Screen.SEARCH.route) },
                onNavigateToSettings = { navController.navigate(Screen.SETTINGS.route) }
            )
        }
        composable(Screen.SEARCH.route) {
            SearchScreen(onBackClick = { navController.popBackStack() })
        }
        composable(Screen.SETTINGS.route) {
            SettingsScreen(onBackClick = { navController.popBackStack() })
        }
    }
}

@Composable
fun MainScreen(onNavigateToSearch: () -> Unit, onNavigateToSettings: () -> Unit) {
    val darkTheme = isSystemInDarkTheme()
    val backgroundColor = if (darkTheme) Color(0xFF141419) else Color(0xFFF2F2F2)
    val menuBackgroundColor = if (darkTheme) Color(0xFF1B1C20) else Color.White
    val textColor = if (darkTheme) Color(0xFFE7E7E7) else Color(0xFF2A2A2A)
    val iconColor = if (darkTheme) Color.White else Color(0xFF2A2A2A)
    val arrowColor = if (darkTheme) Color(0xFF8A8A8A) else Color(0xFFA0A0A0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = (-20).dp)
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .background(menuBackgroundColor)
                .padding(top = 20.dp + 24.dp)
        ) {
            MainMenuItem(
                icon = Icons.Default.Search,
                text = "Поиск",
                iconColor = iconColor,
                textColor = textColor,
                arrowColor = arrowColor,
                onClick = onNavigateToSearch
            )
            MainMenuItem(
                icon = Icons.AutoMirrored.Filled.List,
                text = "Плейлисты",
                iconColor = iconColor,
                textColor = textColor,
                arrowColor = arrowColor
            ) { /* TODO */ }
            MainMenuItem(
                icon = Icons.Default.FavoriteBorder,
                text = "Избранное",
                iconColor = iconColor,
                textColor = textColor,
                arrowColor = arrowColor
            ) { /* TODO */ }
            MainMenuItem(
                icon = Icons.Default.Settings,
                text = "Настройки",
                iconColor = iconColor,
                textColor = textColor,
                arrowColor = arrowColor,
                onClick = onNavigateToSettings
            )
        }
    }
}

@Composable
fun MainMenuItem(
    icon: ImageVector,
    text: String,
    iconColor: Color,
    textColor: Color,
    arrowColor: Color,
    onClick: () -> Unit
) {
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
            tint = iconColor
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            color = textColor,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Go",
            tint = arrowColor
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(onBackClick: () -> Unit) {
    val darkTheme = isSystemInDarkTheme()
    var searchQuery by remember { mutableStateOf("") }

    val backgroundColor = if (darkTheme) Color(0xFF1B1C20) else Color.White
    val textColor = if (darkTheme) Color.White else Color.Black
    val searchFieldBackgroundColor = if (darkTheme) Color(0xFF2C2C2E) else Color(0xFFEFEFF4)
    val placeholderColor = Color(0xFF8E8E93)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Поиск") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = textColor
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = backgroundColor,
                    titleContentColor = textColor
                )
            )
        },
        containerColor = backgroundColor
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Поиск", color = placeholderColor) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon", tint = placeholderColor) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear search", tint = placeholderColor)
                        }
                    }
                },
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = searchFieldBackgroundColor,
                    unfocusedContainerColor = searchFieldBackgroundColor,
                    disabledContainerColor = searchFieldBackgroundColor,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    cursorColor = textColor,
                    focusedTextColor = textColor,
                    unfocusedTextColor = textColor
                )
            )
        }
    }
}

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
            TopAppBar(
                title = { Text(stringResource(id = R.string.settings_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
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
