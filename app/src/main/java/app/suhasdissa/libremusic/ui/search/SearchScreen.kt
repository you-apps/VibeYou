package app.suhasdissa.libremusic.ui.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
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
    LaunchedEffect(focusRequester) {
        focusRequester.requestFocus()
        delay(100)
        keyboard?.show()
        pipedSearchViewModel.setSearchHistory()
    }
    Column(modifier.fillMaxSize()) {
        var expanded by remember { mutableStateOf(false) }

        TextField(
            value = search,
            onValueChange = {
                search = it
                if (search.length >= 3) {
                    pipedSearchViewModel.getSuggestions(search)
                    expanded = true
                }
            },
            modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .focusRequester(focusRequester),

            singleLine = true,
            placeholder = { Text("Search Songs") },
            shape = CircleShape,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                keyboard?.hide()
                expanded = false
                if (search.isNotEmpty()) {
                    pipedSearchViewModel.searchPiped(search)
                }
            })
        )
        Box {
            if (expanded) {
                Surface(
                    modifier = Modifier.zIndex(1f),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shadowElevation = 2.dp
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        pipedSearchViewModel.suggestions.forEach { suggestion ->
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