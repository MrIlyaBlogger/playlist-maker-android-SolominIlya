package com.example.playlist_maker_android_solominilya.ui.navigation

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Headset
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.playlist_maker_android_solominilya.R
import com.example.playlist_maker_android_solominilya.domain.models.Track
import com.example.playlist_maker_android_solominilya.domain.models.Word
import com.example.playlist_maker_android_solominilya.ui.screen.FavoritesScreen
import com.example.playlist_maker_android_solominilya.ui.screen.NewPlaylistScreen
import com.example.playlist_maker_android_solominilya.ui.screen.PlaylistScreen
import com.example.playlist_maker_android_solominilya.ui.screen.PlaylistsScreen
import com.example.playlist_maker_android_solominilya.ui.screen.TrackDetailsScreen
import com.example.playlist_maker_android_solominilya.ui.viewmodel.PlaylistViewModel
import com.example.playlist_maker_android_solominilya.ui.viewmodel.PlaylistsViewModel
import com.example.playlist_maker_android_solominilya.ui.viewmodel.SearchState
import com.example.playlist_maker_android_solominilya.ui.viewmodel.SearchViewModel
import com.example.playlist_maker_android_solominilya.ui.viewmodel.TrackDetailsViewModel

@Composable
fun PlaylistHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.MAIN.route) {
        composable(Screen.MAIN.route) {
            MainScreen(
                onNavigateToSearch = { navController.navigate(Screen.SEARCH.route) },
                onNavigateToSettings = { navController.navigate(Screen.SETTINGS.route) },
                onNavigateToPlaylists = { navController.navigate(Screen.PLAYLISTS.route) },
                onNavigateToFavorites = { navController.navigate(Screen.FAVORITES.route) }
            )
        }
        composable(Screen.SEARCH.route) {
            val searchViewModel: SearchViewModel = viewModel(factory = SearchViewModel.getViewModelFactory())
            SearchScreen(
                viewModel = searchViewModel,
                onBackClick = { navController.popBackStack() },
                onTrackClick = { track -> navController.navigate("track_details/${track.trackId}") }
            )
        }
        composable(Screen.SETTINGS.route) {
            SettingsScreen(onBackClick = { navController.popBackStack() })
        }
        composable(Screen.FAVORITES.route) {
            FavoritesScreen(
                onBackClick = { navController.popBackStack() },
                onTrackClick = { trackId -> navController.navigate("track_details/$trackId") }
            )
        }
        composable(Screen.PLAYLISTS.route) {
            val playlistsViewModel: PlaylistsViewModel = viewModel()
            PlaylistsScreen(
                viewModel = playlistsViewModel,
                onAddPlaylist = { navController.navigate(Screen.NEW_PLAYLIST.route) },
                onBackClick = { navController.popBackStack() },
                onPlaylistClick = { playlistId -> navController.navigate("playlist_details/$playlistId") }
            )
        }
        composable(Screen.NEW_PLAYLIST.route) {
            val playlistsViewModel: PlaylistsViewModel = viewModel(
                viewModelStoreOwner = navController.getBackStackEntry(Screen.PLAYLISTS.route)
            )
            NewPlaylistScreen(
                onSave = { name, description -> playlistsViewModel.createNewPlaylist(name, description) },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(
            route = Screen.PLAYLIST_DETAILS.route,
            arguments = listOf(navArgument("playlistId") { type = NavType.LongType })
        ) { backStackEntry ->
            val playlistId = backStackEntry.arguments?.getLong("playlistId") ?: return@composable
            val playlistViewModel: PlaylistViewModel = viewModel(factory = PlaylistViewModel.factory(playlistId))
            PlaylistScreen(
                playlistViewModel = playlistViewModel,
                onBackClick = { navController.popBackStack() },
                onTrackClick = { track -> navController.navigate("track_details/${track.trackId}") }
            )
        }
        composable(
            route = Screen.TRACK_DETAILS.route,
            arguments = listOf(navArgument("trackId") { type = NavType.LongType })
        ) { backStackEntry ->
            val trackId = backStackEntry.arguments?.getLong("trackId") ?: return@composable
            val trackDetailsViewModel: TrackDetailsViewModel = viewModel()
            val playlistsViewModel: PlaylistsViewModel = viewModel()
            TrackDetailsScreen(
                trackId = trackId,
                trackDetailsViewModel = trackDetailsViewModel,
                playlistsViewModel = playlistsViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}

@Composable
fun MainScreen(
    onNavigateToSearch: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToPlaylists: () -> Unit,
    onNavigateToFavorites: () -> Unit
) {
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
            Text(text = stringResource(R.string.app_title), color = Color.White, fontSize = 22.sp)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = (-20).dp)
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .background(menuBackgroundColor)
                .padding(top = 44.dp)
        ) {
            MainMenuItem(Icons.Default.Search, stringResource(R.string.menu_search), iconColor, textColor, arrowColor, onNavigateToSearch)
            MainMenuItem(Icons.AutoMirrored.Filled.List, stringResource(R.string.menu_playlists), iconColor, textColor, arrowColor, onNavigateToPlaylists)
            MainMenuItem(Icons.Default.FavoriteBorder, stringResource(R.string.menu_favorites), iconColor, textColor, arrowColor, onNavigateToFavorites)
            MainMenuItem(Icons.Default.Settings, stringResource(R.string.menu_settings), iconColor, textColor, arrowColor, onNavigateToSettings)
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
        Icon(imageVector = icon, contentDescription = text, tint = iconColor)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, color = textColor, fontSize = 16.sp, modifier = Modifier.weight(1f))
        Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = arrowColor)
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
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
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
            SettingsItem(title = stringResource(id = R.string.dark_theme), icon = null, isSwitchVisible = true)
            SettingsItem(title = stringResource(id = R.string.share_app), icon = Icons.Default.Share) {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, shareMessage)
                }
                context.startActivity(Intent.createChooser(intent, null))
            }
            SettingsItem(title = stringResource(id = R.string.write_to_support), icon = Icons.Outlined.Headset) {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = "mailto:".toUri()
                    putExtra(Intent.EXTRA_EMAIL, arrayOf("mr.ilyablogger@ya.ru"))
                    putExtra(Intent.EXTRA_SUBJECT, supportSubject)
                    putExtra(Intent.EXTRA_TEXT, supportBody)
                }
                context.startActivity(intent)
            }
            SettingsItem(title = stringResource(id = R.string.user_agreement), icon = Icons.AutoMirrored.Filled.KeyboardArrowRight) {
                val intent = Intent(Intent.ACTION_VIEW).apply { data = agreementUrl.toUri() }
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
                onCheckedChange = { switchState = it },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color(0xFF3D6BE5),
                    uncheckedThumbColor = if (darkTheme) Color(0xFF717171) else Color(0xFFC0C0C0),
                    uncheckedTrackColor = if (darkTheme) Color(0xFF2B2B2F) else Color(0xFFE3E3E3)
                )
            )
        } else if (icon != null) {
            Icon(imageVector = icon, contentDescription = title, tint = iconColor)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onBackClick: () -> Unit,
    onTrackClick: (Track) -> Unit
) {
    val screenState by viewModel.searchScreenState.collectAsState()
    var text by remember { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }
    val historyList by viewModel.historyState.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(text) { viewModel.updateQuery(text) }

    val darkTheme = isSystemInDarkTheme()
    val backgroundColor = if (darkTheme) Color(0xFF1B1C20) else Color.White
    val textColor = if (darkTheme) Color.White else Color.Black
    val searchFieldBackgroundColor = if (darkTheme) Color(0xFF2C2C2E) else Color(0xFFEFEFF4)
    val placeholderColor = Color(0xFF8E8E93)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.search_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад", tint = textColor)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = backgroundColor, titleContentColor = textColor)
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
            OutlinedTextField(
                value = text,
                onValueChange = { newText -> text = newText },
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { isFocused = it.isFocused },
                placeholder = { Text(stringResource(R.string.search_placeholder), color = placeholderColor) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Поиск", tint = placeholderColor) },
                trailingIcon = {
                    if (text.isNotEmpty()) {
                        IconButton(onClick = {
                            text = ""
                            viewModel.clearSearch()
                            keyboardController?.hide()
                        }) {
                            Icon(Icons.Default.Close, contentDescription = "Очистить", tint = placeholderColor)
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

            Spacer(modifier = Modifier.height(8.dp))
            if (isFocused && text.isEmpty() && historyList.isNotEmpty()) {
                HistoryRequests(historyList = historyList, onClick = { word ->
                    text = word
                    viewModel.updateQuery(word)
                })
            }

            when (screenState) {
                is SearchState.Initial -> {
                    if (text.isEmpty()) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(stringResource(R.string.search_initial_message), color = textColor)
                        }
                    } else {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                }
                is SearchState.Searching -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is SearchState.Success -> {
                    val tracks = (screenState as SearchState.Success).foundList
                    if (tracks.isEmpty()) {
                        EmptyPlaceholder()
                    } else {
                        LazyColumn(Modifier.fillMaxSize()) {
                            items(tracks) { track ->
                                TrackListItem(track = track, onClick = { onTrackClick(track) })
                            }
                        }
                    }
                }
                is SearchState.Fail -> {
                    val fail = screenState as SearchState.Fail
                    ErrorPlaceholder(message = fail.error, onRetry = { viewModel.search(fail.retryQuery) })
                }
            }
        }
    }
}

@Composable
fun HistoryRequests(
    historyList: List<Word>,
    onClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 200.dp)
    ) {
        items(historyList) { word ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onClick(word.word) }
                    .padding(vertical = 12.dp, horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Filled.History, contentDescription = null, tint = Color.Gray)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = word.word, color = Color.Gray)
            }
            HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
        }
    }
}

@Composable
fun TrackListItem(track: Track, onLongClick: (() -> Unit)? = null, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(track.artworkUrl100)
                .crossfade(true)
                .build(),
            contentDescription = track.trackName,
            modifier = Modifier
                .size(45.dp)
                .clip(RoundedCornerShape(4.dp)),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.ic_music),
            error = painterResource(id = R.drawable.ic_music)
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp)
        ) {
            Text(track.trackName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(
                text = "${track.artistName} · ${track.formattedTime}",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
        Text(
            text = ">",
            fontSize = 18.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun EmptyPlaceholder() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_empty),
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = Color.Gray
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("Ничего не найдено", color = Color.Gray)
    }
}

@Composable
fun ErrorPlaceholder(message: String, onRetry: (() -> Unit)?) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_error),
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = Color.Gray
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(message, color = Color.Gray)
        if (onRetry != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRetry) {
                Text("Обновить")
            }
        }
    }
}
