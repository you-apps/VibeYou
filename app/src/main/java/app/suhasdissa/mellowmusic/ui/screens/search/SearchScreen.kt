package app.suhasdissa.mellowmusic.ui.screens.search

import android.view.SoundEffectConstants
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import app.suhasdissa.mellowmusic.Destination
import app.suhasdissa.mellowmusic.R
import app.suhasdissa.mellowmusic.backend.data.Song
import app.suhasdissa.mellowmusic.backend.viewmodel.PipedSearchViewModel
import app.suhasdissa.mellowmusic.backend.viewmodel.PlayerViewModel
import app.suhasdissa.mellowmusic.backend.viewmodel.state.SearchState
import app.suhasdissa.mellowmusic.ui.components.AlbumList
import app.suhasdissa.mellowmusic.ui.components.ArtistList
import app.suhasdissa.mellowmusic.ui.components.ChipSelector
import app.suhasdissa.mellowmusic.ui.components.IllustratedMessageScreen
import app.suhasdissa.mellowmusic.ui.components.LoadingScreen
import app.suhasdissa.mellowmusic.ui.components.MiniPlayerScaffold
import app.suhasdissa.mellowmusic.ui.components.SongCard
import app.suhasdissa.mellowmusic.ui.components.SongList
import app.suhasdissa.mellowmusic.ui.components.SongSettingsSheetSearchPage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onNavigate: (Destination) -> Unit,
    pipedSearchViewModel: PipedSearchViewModel = viewModel(factory = PipedSearchViewModel.Factory),
    playerViewModel: PlayerViewModel = viewModel(factory = PlayerViewModel.Factory)
) {
    var isPopupOpen by remember {
        mutableStateOf(pipedSearchViewModel.state !is SearchState.Success)
    }
    var showSongSettings by remember { mutableStateOf(false) }
    var selectedSong by remember { mutableStateOf<Song?>(null) }
    val view = LocalView.current
    MiniPlayerScaffold {
        Column(Modifier.fillMaxSize()) {
            Row(modifier = Modifier.fillMaxWidth()) {
                SearchBar(
                    modifier = Modifier
                        .weight(1f),
                    query = pipedSearchViewModel.search,
                    onQueryChange = {
                        pipedSearchViewModel.search = it
                        if (it.length > 3) pipedSearchViewModel.getSuggestions()
                    },
                    onSearch = {
                        pipedSearchViewModel.searchPiped()
                        isPopupOpen = false
                    },
                    placeholder = {
                        Text(
                            stringResource(id = R.string.search_songs)
                        )
                    },
                    active = isPopupOpen,
                    onActiveChange = {
                        isPopupOpen = it
                    },
                    leadingIcon = {
                        if (isPopupOpen) {
                            IconButton(onClick = { isPopupOpen = false }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = stringResource(R.string.close_search)
                                )
                            }
                        } else {
                            Icon(
                                modifier = Modifier.size(48.dp),
                                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                                contentDescription = null
                            )
                        }
                    },
                    trailingIcon = {
                        if (isPopupOpen) {
                            IconButton(onClick = {
                                view.playSoundEffect(SoundEffectConstants.CLICK)
                                pipedSearchViewModel.search = ""
                            }) {
                                Icon(
                                    Icons.Default.Clear,
                                    contentDescription = stringResource(R.string.clear_search)
                                )
                            }
                        } else {
                            Icon(
                                modifier = Modifier.padding(8.dp),
                                imageVector = Icons.Default.Search,
                                contentDescription = null
                            )
                        }
                    }
                ) {
                    LaunchedEffect(Unit) {
                        pipedSearchViewModel.setSearchHistory()
                    }
                    val scroll = rememberScrollState()
                    Column(Modifier.verticalScroll(scroll).padding(horizontal = 8.dp)) {
                        if (pipedSearchViewModel.suggestions.isNotEmpty()) {
                            pipedSearchViewModel.suggestions.forEach {
                                ListItem(
                                    modifier = Modifier.clickable {
                                        pipedSearchViewModel.search = it
                                        pipedSearchViewModel.searchPiped()
                                        isPopupOpen = false
                                    },
                                    headlineContent = { Text(it) },
                                    leadingContent = {
                                        Icon(
                                            imageVector = Icons.Default.Search,
                                            contentDescription = null
                                        )
                                    }
                                )
                            }
                        } else {
                            pipedSearchViewModel.history.forEach {
                                ListItem(
                                    modifier = Modifier.clickable {
                                        pipedSearchViewModel.search = it
                                        pipedSearchViewModel.searchPiped()
                                        isPopupOpen = false
                                    },
                                    headlineContent = { Text(it) },
                                    leadingContent = {
                                        Icon(
                                            imageVector = Icons.Default.History,
                                            contentDescription = null
                                        )
                                    }
                                )
                            }
                        }
                        if (pipedSearchViewModel.songSearchSuggestion.isNotEmpty()) {
                            Text(
                                text = stringResource(R.string.recent_songs),
                                style = MaterialTheme.typography.titleSmall
                            )
                            pipedSearchViewModel.songSearchSuggestion.forEach { item ->
                                SongCard(
                                    song = item,
                                    onClickCard = {
                                        playerViewModel.playSong(item)
                                    },
                                    onLongPress = {
                                        selectedSong = item
                                        showSongSettings = true
                                    }
                                )
                            }
                        }
                    }
                }
            }
            Column {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    ChipSelector(onItemSelected = {
                        pipedSearchViewModel.searchFilter = it
                        pipedSearchViewModel.searchPiped()
                    }, defaultValue = pipedSearchViewModel.searchFilter)
                }
                when (val searchState = pipedSearchViewModel.state) {
                    is SearchState.Loading -> {
                        LoadingScreen()
                    }

                    is SearchState.Error -> {
                        IllustratedMessageScreen(
                            image = R.drawable.sad_mellow,
                            message = R.string.something_went_wrong
                        )
                    }

                    is SearchState.Success -> {
                        when (searchState) {
                            is SearchState.Success.Playlists -> {
                                AlbumList(
                                    items = searchState.items,
                                    onClickCard = {
                                        pipedSearchViewModel.getPlaylistInfo(it)
                                        onNavigate(Destination.Playlists)
                                    },
                                    onLongPress = {
                                    }
                                )
                            }

                            is SearchState.Success.Songs -> {
                                SongList(
                                    items = searchState.items,
                                    onClickCard = { song ->
                                        playerViewModel.playSong(song)
                                        playerViewModel.saveSong(song)
                                    },
                                    onLongPress = { song ->
                                        selectedSong = song
                                        showSongSettings = true
                                    }
                                )
                            }

                            is SearchState.Success.Artists -> {
                                ArtistList(
                                    items = searchState.items,
                                    onClickCard = {
                                        pipedSearchViewModel.getChannelInfo(it)
                                        onNavigate(Destination.Artist)
                                    },
                                    onLongPress = {
                                    }
                                )
                            }
                        }
                    }

                    is SearchState.Empty -> {
                        IllustratedMessageScreen(
                            image = R.drawable.ic_launcher_monochrome,
                            message = R.string.search_for_a_song,
                            messageColor = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
            }
        }
    }
    if (showSongSettings) {
        selectedSong?.let {
            SongSettingsSheetSearchPage(
                onDismissRequest = { showSongSettings = false },
                song = selectedSong!!
            )
        }
    }
}
