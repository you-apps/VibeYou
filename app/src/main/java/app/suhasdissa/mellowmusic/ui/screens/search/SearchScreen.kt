package app.suhasdissa.mellowmusic.ui.screens.search

import android.view.SoundEffectConstants
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import app.suhasdissa.mellowmusic.Artist
import app.suhasdissa.mellowmusic.Destinations
import app.suhasdissa.mellowmusic.Playlists
import app.suhasdissa.mellowmusic.R
import app.suhasdissa.mellowmusic.backend.database.entities.Song
import app.suhasdissa.mellowmusic.backend.viewmodel.ArtistViewModel
import app.suhasdissa.mellowmusic.backend.viewmodel.PipedSearchViewModel
import app.suhasdissa.mellowmusic.backend.viewmodel.PlayerViewModel
import app.suhasdissa.mellowmusic.backend.viewmodel.PlaylistViewModel
import app.suhasdissa.mellowmusic.backend.viewmodel.state.PipedSearchState
import app.suhasdissa.mellowmusic.ui.components.AlbumList
import app.suhasdissa.mellowmusic.ui.components.ArtistList
import app.suhasdissa.mellowmusic.ui.components.ChipSelector
import app.suhasdissa.mellowmusic.ui.components.IllustratedMessageScreen
import app.suhasdissa.mellowmusic.ui.components.LoadingScreen
import app.suhasdissa.mellowmusic.ui.components.MiniPlayerScaffold
import app.suhasdissa.mellowmusic.ui.components.SongList
import app.suhasdissa.mellowmusic.ui.components.SongSettingsSheetSearchPage
import kotlinx.coroutines.delay

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(
    onNavigate: (Destinations) -> Unit,
    playlistViewModel: PlaylistViewModel = viewModel(factory = PlaylistViewModel.Factory),
    pipedSearchViewModel: PipedSearchViewModel = viewModel(factory = PipedSearchViewModel.Factory),
    playerViewModel: PlayerViewModel = viewModel(factory = PlayerViewModel.Factory),
    artistViewModel: ArtistViewModel = viewModel(factory = ArtistViewModel.Factory)
) {
    var isPopupOpen by remember {
        mutableStateOf(pipedSearchViewModel.state !is PipedSearchState.Success)
    }
    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current
    val view = LocalView.current

    LaunchedEffect(focusRequester) {
        focusRequester.requestFocus()
        delay(100)
        keyboard?.show()
        pipedSearchViewModel.setSearchHistory()
    }
    MiniPlayerScaffold {
        Column(Modifier.fillMaxSize()) {
            TextField(
                value = pipedSearchViewModel.search,
                onValueChange = {
                    pipedSearchViewModel.search = it
                    if (!isPopupOpen) isPopupOpen = true
                    pipedSearchViewModel.getSuggestions()
                },
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .focusRequester(focusRequester),

                singleLine = true,
                placeholder = { Text(stringResource(R.string.search_songs)) },
                shape = CircleShape,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    keyboard?.hide()
                    if (isPopupOpen) isPopupOpen = false
                    pipedSearchViewModel.searchPiped()
                }),
                trailingIcon = {
                    IconButton(onClick = {
                        view.playSoundEffect(SoundEffectConstants.CLICK)
                        pipedSearchViewModel.search = ""
                        isPopupOpen = false
                    }) {
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = stringResource(R.string.clear_search)
                        )
                    }
                }
            )
            Box {
                if (isPopupOpen) {
                    Surface(
                        modifier = Modifier.zIndex(2f),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shadowElevation = 2.dp
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 400.dp)
                        ) {
                            items(items = pipedSearchViewModel.suggestions) { suggestion ->
                                DropdownMenuItem(text = {
                                    Text(
                                        text = suggestion,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }, onClick = {
                                    view.playSoundEffect(SoundEffectConstants.CLICK)
                                    pipedSearchViewModel.search = suggestion
                                    keyboard?.hide()
                                    isPopupOpen = false
                                    pipedSearchViewModel.searchPiped()
                                })
                            }
                        }
                    }
                }
                when (val searchState = pipedSearchViewModel.state) {
                    is PipedSearchState.Loading -> {
                        LoadingScreen()
                    }

                    is PipedSearchState.Error -> {
                        IllustratedMessageScreen(
                            image = R.drawable.sad_mellow,
                            message = R.string.something_went_wrong
                        )
                    }

                    is PipedSearchState.Success -> {
                        var showSongSettings by remember { mutableStateOf(false) }
                        var selectedSong by remember { mutableStateOf<Song?>(null) }

                        Column {
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                                ChipSelector(onItemSelected = {
                                    pipedSearchViewModel.searchFilter = it
                                    pipedSearchViewModel.searchPiped()
                                }, defaultValue = pipedSearchViewModel.searchFilter)
                            }
                            when (searchState) {
                                is PipedSearchState.Success.Playlists -> {
                                    AlbumList(
                                        items = searchState.items,
                                        onClickCard = {
                                            playlistViewModel.getPlaylistInfo(it.playlistId)
                                            onNavigate(Playlists)
                                        },
                                        onLongPress = {
                                        }
                                    )
                                }

                                is PipedSearchState.Success.Songs -> {
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

                                is PipedSearchState.Success.Artists -> {
                                    ArtistList(
                                        items = searchState.items,
                                        onClickCard = {
                                            artistViewModel.getChannelInfo(it.artistId)
                                            onNavigate(Artist)
                                        },
                                        onLongPress = {
                                        }
                                    )
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

                    is PipedSearchState.Empty -> {
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
}
