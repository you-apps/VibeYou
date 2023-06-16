package app.suhasdissa.libremusic.ui.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import app.suhasdissa.libremusic.backend.viewmodel.PlayerViewModel
import app.suhasdissa.libremusic.backend.viewmodel.SongViewModel
import app.suhasdissa.libremusic.ui.components.SongList

@Composable
fun SongsScreen(showFavourites:Boolean,
    songViewModel: SongViewModel = viewModel(factory = SongViewModel.Factory),
    playerViewModel: PlayerViewModel = viewModel(factory = PlayerViewModel.Factory)
) {
    LaunchedEffect(Unit) {
        if(showFavourites){
            songViewModel.getFavouriteSongs()
        }else {
            songViewModel.getAllSongs()
        }
    }
    songViewModel.songs?.let { songs ->
        SongList(items = songs, onClickVideoCard = { song -> playerViewModel.playSong(song) })
    }
}