package app.suhasdissa.vibeyou.presentation.screens.localsearch

import android.view.SoundEffectConstants
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Search
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
import app.suhasdissa.vibeyou.R
import app.suhasdissa.vibeyou.domain.models.primary.Song
import app.suhasdissa.vibeyou.navigation.Destination
import app.suhasdissa.vibeyou.presentation.components.ChipSelector
import app.suhasdissa.vibeyou.presentation.components.IllustratedMessageScreen
import app.suhasdissa.vibeyou.presentation.components.LoadingScreen
import app.suhasdissa.vibeyou.presentation.components.SongCard
import app.suhasdissa.vibeyou.presentation.components.SongList
import app.suhasdissa.vibeyou.presentation.components.SongSettingsSheetSearchPage
import app.suhasdissa.vibeyou.presentation.screens.album.components.AlbumList
import app.suhasdissa.vibeyou.presentation.screens.artist.components.ArtistList
import app.suhasdissa.vibeyou.presentation.screens.localsearch.model.LocalSearchViewModel
import app.suhasdissa.vibeyou.presentation.screens.onlinesearch.model.state.SearchState
import app.suhasdissa.vibeyou.presentation.screens.player.model.PlayerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocalSearchScreen(
    onNavigate: (Destination) -> Unit,
    playerViewModel: PlayerViewModel,
    localSearchViewModel: LocalSearchViewModel = viewModel(factory = LocalSearchViewModel.Factory)
) {
    var isPopupOpen by remember {
        mutableStateOf(localSearchViewModel.state !is SearchState.Success)
    }
    var showSongSettings by remember { mutableStateOf(false) }
    var selectedSong by remember { mutableStateOf<Song?>(null) }
    val view = LocalView.current
    Column(Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            SearchBar(
                modifier = Modifier
                    .weight(1f),
                query = localSearchViewModel.search,
                onQueryChange = {
                    localSearchViewModel.search = it
                    if (it.length > 3) localSearchViewModel.getSuggestions()
                },
                onSearch = {
                    localSearchViewModel.searchPiped()
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
                                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
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
                            localSearchViewModel.search = ""
                        }) {
                            Icon(
                                Icons.Rounded.Clear,
                                contentDescription = stringResource(R.string.clear_search)
                            )
                        }
                    } else {
                        Icon(
                            modifier = Modifier.padding(8.dp),
                            imageVector = Icons.Rounded.Search,
                            contentDescription = null
                        )
                    }
                }
            ) {
                LaunchedEffect(Unit) {
                    localSearchViewModel.setSearchHistory()
                }
                val scroll = rememberScrollState()
                Column(
                    Modifier
                        .verticalScroll(scroll)
                ) {
                    if (localSearchViewModel.songSearchSuggestion.isNotEmpty()) {
                        Text(
                            text = stringResource(R.string.songs),
                            style = MaterialTheme.typography.titleSmall
                        )
                        localSearchViewModel.songSearchSuggestion.forEach { item ->
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
                    localSearchViewModel.history.forEach {
                        ListItem(
                            modifier = Modifier.clickable {
                                localSearchViewModel.search = it
                                localSearchViewModel.searchPiped()
                                isPopupOpen = false
                            },
                            headlineContent = { Text(it) },
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Rounded.History,
                                    contentDescription = null
                                )
                            },
                            trailingContent = {
                                IconButton(
                                    modifier = Modifier.offset(x = 16.dp),
                                    onClick = {
                                        localSearchViewModel.deleteFromHistory(it)
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Close,
                                        contentDescription = null
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
        Column {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                ChipSelector(onItemSelected = {
                    localSearchViewModel.searchFilter = it
                    localSearchViewModel.searchPiped()
                }, defaultValue = localSearchViewModel.searchFilter)
            }
            when (val searchState = localSearchViewModel.state) {
                is SearchState.Loading -> {
                    LoadingScreen()
                }

                is SearchState.Error -> {
                    IllustratedMessageScreen(
                        image = R.drawable.ic_launcher_monochrome,
                        message = R.string.something_went_wrong
                    )
                }

                is SearchState.Success -> {
                    when (searchState) {
                        is SearchState.Success.Playlists -> {
                            AlbumList(
                                items = searchState.items,
                                onClickCard = {
                                    onNavigate(Destination.LocalPlaylists(it))
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
                                    onNavigate(Destination.LocalArtist(it))
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
    if (showSongSettings) {
        selectedSong?.let {
            SongSettingsSheetSearchPage(
                onDismissRequest = { showSongSettings = false },
                song = selectedSong!!,
                playerViewModel
            )
        }
    }
}
