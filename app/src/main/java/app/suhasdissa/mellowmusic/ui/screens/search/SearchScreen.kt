package app.suhasdissa.mellowmusic.ui.screens.search

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import app.suhasdissa.mellowmusic.R
import app.suhasdissa.mellowmusic.backend.database.entities.Song
import app.suhasdissa.mellowmusic.backend.viewmodel.PipedSearchViewModel
import app.suhasdissa.mellowmusic.backend.viewmodel.PlayerViewModel
import app.suhasdissa.mellowmusic.ui.components.IllustratedMessageScreen
import app.suhasdissa.mellowmusic.ui.components.LoadingScreen
import app.suhasdissa.mellowmusic.ui.components.MiniPlayerScaffold
import app.suhasdissa.mellowmusic.ui.components.SongList
import app.suhasdissa.mellowmusic.ui.components.SongSettingsSheetSearchPage
import app.suhasdissa.mellowmusic.ui.components.SpinnerSelector
import kotlinx.coroutines.delay

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    pipedSearchViewModel: PipedSearchViewModel = viewModel(factory = PipedSearchViewModel.Factory),
    playerViewModel: PlayerViewModel = viewModel(factory = PlayerViewModel.Factory)
) {
    var search by remember { mutableStateOf(pipedSearchViewModel.searchText) }
    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current
    LaunchedEffect(focusRequester) {
        focusRequester.requestFocus()
        delay(100)
        keyboard?.show()
        pipedSearchViewModel.setSearchHistory()
    }
    MiniPlayerScaffold {
        Column(modifier.fillMaxSize()) {
            var expanded by remember { mutableStateOf(false) }

            TextField(
                value = search,
                onValueChange = {
                    search = it
                    expanded = true
                    if (search.length >= 3) {
                        pipedSearchViewModel.getSuggestions(search)
                    }
                },
                modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .focusRequester(focusRequester),

                singleLine = true,
                placeholder = { Text(stringResource(R.string.search_songs)) },
                shape = CircleShape,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    keyboard?.hide()
                    expanded = false
                    if (search.isNotEmpty()) {
                        pipedSearchViewModel.searchPiped(search)
                    }
                }),
                trailingIcon = {
                    IconButton(onClick = { search = "" }) {
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = stringResource(R.string.clear_search)
                        )
                    }
                }
            )
            Box {
                if (expanded) {
                    Surface(
                        modifier = Modifier.zIndex(1f),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shadowElevation = 2.dp
                    ) {
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth().heightIn(max = 400.dp)
                        ) {
                            items(items = pipedSearchViewModel.suggestions.reversed()) { suggestion ->
                                DropdownMenuItem(text = {
                                    Text(
                                        text = suggestion,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }, onClick = {
                                    search = suggestion
                                    keyboard?.hide()
                                    expanded = false
                                    if (search.isNotEmpty()) {
                                        pipedSearchViewModel.searchPiped(search)
                                    }
                                })
                            }
                        }
                    }
                }
                when (val searchState = pipedSearchViewModel.state) {
                    is PipedSearchViewModel.PipedSearchState.Loading -> {
                        LoadingScreen()
                    }

                    is PipedSearchViewModel.PipedSearchState.Error -> {
                        IllustratedMessageScreen(
                            image = R.drawable.sad_mellow,
                            message = R.string.something_went_wrong
                        )
                    }

                    is PipedSearchViewModel.PipedSearchState.Success -> {
                        var showSongSettings by remember { mutableStateOf(false) }
                        var selectedSong by remember { mutableStateOf<Song?>(null) }

                        Column {
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                                SpinnerSelector(onItemSelected = {
                                    pipedSearchViewModel.searchFilter = it
                                    pipedSearchViewModel.searchPiped(search)
                                }, defaultValue = pipedSearchViewModel.searchFilter)
                            }
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

                        if (showSongSettings) {
                            selectedSong?.let {
                                SongSettingsSheetSearchPage(
                                    onDismissRequest = { showSongSettings = false },
                                    song = selectedSong!!
                                )
                            }
                        }
                    }

                    is PipedSearchViewModel.PipedSearchState.Empty -> {
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
