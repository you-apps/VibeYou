package app.suhasdissa.libremusic.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import app.suhasdissa.libremusic.backend.viewmodel.PipedSearchViewModel
import app.suhasdissa.libremusic.backend.viewmodel.PlayerViewModel
import app.suhasdissa.libremusic.ui.components.ErrorScreen
import app.suhasdissa.libremusic.ui.components.InfoScreen
import app.suhasdissa.libremusic.ui.components.LoadingScreen
import app.suhasdissa.libremusic.ui.components.SongList
import kotlinx.coroutines.delay

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    pipedSearchViewModel: PipedSearchViewModel = viewModel(factory = PipedSearchViewModel.Factory),
    playerViewModel: PlayerViewModel = viewModel(factory = PlayerViewModel.Factory)
) {
    var search by remember { mutableStateOf(pipedSearchViewModel.searchText) }
    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current
    var showSuggestions by remember { mutableStateOf(false) }
    LaunchedEffect(focusRequester) {
        focusRequester.requestFocus()
        delay(100)
        keyboard?.show()
        showSuggestions = true
        pipedSearchViewModel.setSearchHistory()
    }
    Column(modifier.fillMaxSize()) {
        Row(modifier.padding(horizontal = 10.dp), horizontalArrangement = Arrangement.Center) {
            TextField(
                value = search,
                onValueChange = {
                    search = it
                    if (search.length >= 3) {
                        pipedSearchViewModel.getSuggestions(search)
                    }
                    showSuggestions = true
                },
                modifier
                    .weight(1f)
                    .focusRequester(focusRequester),

                singleLine = true,
                placeholder = { Text("Search Songs") },
                shape = CircleShape
            )
            Button(onClick = {
                keyboard?.hide()
                if (search.isNotEmpty()) {
                    pipedSearchViewModel.searchPiped(search)
                }
            }) {
                Text("Search")
            }
        }

        if (search.isNotEmpty() && showSuggestions) {
            LazyColumn(Modifier.fillMaxSize()) {
                items(items = pipedSearchViewModel.suggestions) { suggestion ->
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .clickable {
                                search = suggestion
                                keyboard?.hide()
                                showSuggestions = false
                                if (search.isNotEmpty()) {
                                    pipedSearchViewModel.searchPiped(search)
                                }
                            }) {
                        Text(text = suggestion, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        } else {
            when (val searchState = pipedSearchViewModel.state) {
                is PipedSearchViewModel.PipedSearchState.Loading -> {
                    LoadingScreen()
                }

                is PipedSearchViewModel.PipedSearchState.Error -> {
                    ErrorScreen(error = searchState.error, onRetry = {
                        if (search.isNotEmpty()) {
                            pipedSearchViewModel.searchPiped(search)
                        }
                    })
                }

                is PipedSearchViewModel.PipedSearchState.Success -> {
                    SongList(
                        items = searchState.items,
                        onClickVideoCard = { song ->

                            playerViewModel.playSong(song)
                        })
                }

                is PipedSearchViewModel.PipedSearchState.Empty -> {
                    InfoScreen(info = "Search For a Song")
                }
            }
        }
    }
}