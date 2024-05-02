package app.suhasdissa.vibeyou.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import app.suhasdissa.vibeyou.domain.models.primary.Album
import app.suhasdissa.vibeyou.domain.models.primary.Artist
import app.suhasdissa.vibeyou.presentation.screens.album.AlbumScreen
import app.suhasdissa.vibeyou.presentation.screens.album.model.LocalPlaylistViewModel
import app.suhasdissa.vibeyou.presentation.screens.album.model.OnlinePlaylistViewModel
import app.suhasdissa.vibeyou.presentation.screens.artist.ArtistScreen
import app.suhasdissa.vibeyou.presentation.screens.artist.model.LocalArtistViewModel
import app.suhasdissa.vibeyou.presentation.screens.artist.model.OnlineArtistViewModel
import app.suhasdissa.vibeyou.presentation.screens.home.HomeScreen
import app.suhasdissa.vibeyou.presentation.screens.localsearch.LocalSearchScreen
import app.suhasdissa.vibeyou.presentation.screens.localsearch.model.LocalSearchViewModel
import app.suhasdissa.vibeyou.presentation.screens.onlinesearch.SearchScreen
import app.suhasdissa.vibeyou.presentation.screens.onlinesearch.model.PipedSearchViewModel
import app.suhasdissa.vibeyou.presentation.screens.playlists.model.PlaylistInfoViewModel
import app.suhasdissa.vibeyou.presentation.screens.settings.AboutScreen
import app.suhasdissa.vibeyou.presentation.screens.settings.AppearanceSettingsScreen
import app.suhasdissa.vibeyou.presentation.screens.settings.DatabaseSettingsScreen
import app.suhasdissa.vibeyou.presentation.screens.settings.NetworkSettingsScreen
import app.suhasdissa.vibeyou.presentation.screens.settings.SettingsScreen
import app.suhasdissa.vibeyou.presentation.screens.settings.model.SettingsModel
import kotlin.reflect.typeOf

@Composable
fun AppNavHost(navHostController: NavHostController) {
    val viewModelStoreOwner = LocalViewModelStoreOwner.current!!

    val settingsModel: SettingsModel = viewModel(factory = SettingsModel.Factory)
    NavHost(
        navController = navHostController,
        startDestination = Destination.PipedMusic
    ) {
        composable<Destination.PipedMusic> {
            CompositionLocalProvider(LocalViewModelStoreOwner provides viewModelStoreOwner) {
                HomeScreen(onNavigate = { destination ->
                    navHostController.navigate(destination)
                })
            }
        }

        composable<Destination.OnlineSearch> {
            val pipedSearchViewModel: PipedSearchViewModel =
                viewModel(factory = PipedSearchViewModel.Factory)
            CompositionLocalProvider(LocalViewModelStoreOwner provides viewModelStoreOwner) {
                SearchScreen(onNavigate = {
                    navHostController.navigate(it)
                }, pipedSearchViewModel)
            }
        }
        composable<Destination.LocalSearch> {
            val localSearchViewModel: LocalSearchViewModel =
                viewModel(factory = LocalSearchViewModel.Factory)
            CompositionLocalProvider(LocalViewModelStoreOwner provides viewModelStoreOwner) {
                LocalSearchScreen(onNavigate = {
                    navHostController.navigate(it)
                }, localSearchViewModel)
            }
        }

        composable<Destination.Settings> {
            SettingsScreen(onNavigate = { route ->
                navHostController.navigate(route)
            })
        }

        composable<Destination.About> {
            AboutScreen()
        }

        composable<Destination.NetworkSettings> {
            NetworkSettingsScreen()
        }

        composable<Destination.DatabaseSettings> {
            DatabaseSettingsScreen()
        }

        composable<Destination.AppearanceSettings> {
            AppearanceSettingsScreen(settingsModel)
        }

        composable<Destination.Playlists>(
            typeMap = mapOf(typeOf<Album>() to AlbumType)
        ) {
            val onlinePlaylistViewModel: OnlinePlaylistViewModel =
                viewModel(factory = OnlinePlaylistViewModel.Factory)
            CompositionLocalProvider(LocalViewModelStoreOwner provides viewModelStoreOwner) {
                AlbumScreen(onlinePlaylistViewModel.albumInfoState)
            }
        }

        composable<Destination.LocalPlaylists>(
            typeMap = mapOf(typeOf<Album>() to AlbumType)
        ) {
            val localPlaylistViewModel: LocalPlaylistViewModel =
                viewModel(factory = LocalPlaylistViewModel.Factory)
            CompositionLocalProvider(LocalViewModelStoreOwner provides viewModelStoreOwner) {
                AlbumScreen(localPlaylistViewModel.albumInfoState)
            }
        }

        composable<Destination.SavedPlaylists>(
            typeMap = mapOf(typeOf<Album>() to AlbumType)
        ) {
            val playlistInfoViewModel: PlaylistInfoViewModel =
                viewModel(factory = PlaylistInfoViewModel.Factory)
            CompositionLocalProvider(LocalViewModelStoreOwner provides viewModelStoreOwner) {
                AlbumScreen(playlistInfoViewModel.albumInfoState)
            }
        }

        composable<Destination.OnlineArtist>(
            typeMap = mapOf(typeOf<Artist>() to ArtistType)
        ) {
            val onlineArtistViewModel: OnlineArtistViewModel =
                viewModel(factory = OnlineArtistViewModel.Factory)
            CompositionLocalProvider(LocalViewModelStoreOwner provides viewModelStoreOwner) {
                ArtistScreen(onClickAlbum = {
                    navHostController.navigate(Destination.Playlists(it))
                }, onlineArtistViewModel.artistInfoState)
            }
        }

        composable<Destination.LocalArtist>(
            typeMap = mapOf(typeOf<Artist>() to ArtistType)
        ) {
            val localArtistViewModel: LocalArtistViewModel =
                viewModel(factory = LocalArtistViewModel.Factory)
            CompositionLocalProvider(LocalViewModelStoreOwner provides viewModelStoreOwner) {
                ArtistScreen(onClickAlbum = {
                    navHostController.navigate(Destination.LocalPlaylists(it))
                }, localArtistViewModel.artistInfoState)
            }
        }
    }
}
