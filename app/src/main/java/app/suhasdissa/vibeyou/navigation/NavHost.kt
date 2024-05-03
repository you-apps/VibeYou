package app.suhasdissa.vibeyou.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hasRoute
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
fun AppNavHost(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    homeNavHostController: NavHostController,
    onDrawerOpen: () -> Unit
) {
    val viewModelStoreOwner = LocalViewModelStoreOwner.current!!

    val settingsModel: SettingsModel = viewModel(factory = SettingsModel.Factory)
    NavHost(
        modifier = modifier,
        navController = navHostController,
        startDestination = Destination.Home
    ) {
        composable<Destination.Home>(
            enterTransition = {
                scaleIn(initialScale = 3f / 4) + fadeIn()
            },
            exitTransition = {
                if (listOf(Destination.OnlineSearch::class, Destination.LocalSearch::class)
                        .any { targetState.destination.hasRoute(it) }
                ) {
                    fadeOut()
                } else {
                    scaleOut(targetScale = 0f) + fadeOut()
                }
            }
        ) {
            CompositionLocalProvider(LocalViewModelStoreOwner provides viewModelStoreOwner) {
                HomeScreen(onNavigate = { destination ->
                    navHostController.navigate(destination)
                }, onDrawerOpen = onDrawerOpen, homeNavHostController)
            }
        }

        composable<Destination.OnlineSearch>(
            enterTransition = {
                fadeIn()
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down,
                    targetOffset = { it }) + fadeOut()
            }
        ) {
            val pipedSearchViewModel: PipedSearchViewModel =
                viewModel(factory = PipedSearchViewModel.Factory)
            CompositionLocalProvider(LocalViewModelStoreOwner provides viewModelStoreOwner) {
                SearchScreen(onNavigate = {
                    navHostController.navigate(it)
                }, pipedSearchViewModel)
            }
        }
        composable<Destination.LocalSearch>(
            enterTransition = {
                fadeIn()
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down,
                    targetOffset = { it }) + fadeOut()
            }
        ) {
            val localSearchViewModel: LocalSearchViewModel =
                viewModel(factory = LocalSearchViewModel.Factory)
            CompositionLocalProvider(LocalViewModelStoreOwner provides viewModelStoreOwner) {
                LocalSearchScreen(onNavigate = {
                    navHostController.navigate(it)
                }, localSearchViewModel)
            }
        }

        composable<Destination.Settings>(
            enterTransition = {
                if (listOf(
                        Destination.About::class,
                        Destination.NetworkSettings::class,
                        Destination.DatabaseSettings::class,
                        Destination.AppearanceSettings::class
                    )
                        .any { initialState.destination.hasRoute(it) }
                ) {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        initialOffset = { it }) + fadeIn()
                } else {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        initialOffset = { it / 4 }) + scaleIn(initialScale = 3f / 4) + fadeIn()
                }
            },
            exitTransition = {
                if (listOf(
                        Destination.About::class,
                        Destination.NetworkSettings::class,
                        Destination.DatabaseSettings::class,
                        Destination.AppearanceSettings::class
                    )
                        .any { targetState.destination.hasRoute(it) }
                ) {
                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left,
                        targetOffset = { it / 4 }) + fadeOut()
                } else {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        targetOffset = { it / 4 }) + scaleOut(targetScale = 3f / 4) + fadeOut()
                }
            }) {
            SettingsScreen(onNavigate = { route ->
                navHostController.navigate(route)
            })
        }

        composable<Destination.About>(
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    initialOffset = { it }) + fadeIn()
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    targetOffset = { it }) + fadeOut()
            }
        ) {
            AboutScreen()
        }

        composable<Destination.NetworkSettings>(
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    initialOffset = { it }) + fadeIn()
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    targetOffset = { it }) + fadeOut()
            }
        ) {
            NetworkSettingsScreen()
        }

        composable<Destination.DatabaseSettings>(
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    initialOffset = { it }) + fadeIn()
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    targetOffset = { it }) + fadeOut()
            }
        ) {
            DatabaseSettingsScreen()
        }

        composable<Destination.AppearanceSettings>(
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    initialOffset = { it }) + fadeIn()
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    targetOffset = { it }) + fadeOut()
            }
        ) {
            AppearanceSettingsScreen(settingsModel)
        }

        composable<Destination.Playlists>(
            typeMap = mapOf(typeOf<Album>() to AlbumType),
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up,
                    initialOffset = { it / 4 }) + scaleIn(initialScale = 3f / 4) + fadeIn()
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down,
                    targetOffset = { it / 4 }) + scaleOut(targetScale = 3f / 4) + fadeOut()
            }
        ) {
            val onlinePlaylistViewModel: OnlinePlaylistViewModel =
                viewModel(factory = OnlinePlaylistViewModel.Factory)
            CompositionLocalProvider(LocalViewModelStoreOwner provides viewModelStoreOwner) {
                AlbumScreen(onlinePlaylistViewModel.albumInfoState)
            }
        }

        composable<Destination.LocalPlaylists>(
            typeMap = mapOf(typeOf<Album>() to AlbumType),
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up,
                    initialOffset = { it / 4 }) + scaleIn(initialScale = 3f / 4) + fadeIn()
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down,
                    targetOffset = { it / 4 }) + scaleOut(targetScale = 3f / 4) + fadeOut()
            }
        ) {
            val localPlaylistViewModel: LocalPlaylistViewModel =
                viewModel(factory = LocalPlaylistViewModel.Factory)
            CompositionLocalProvider(LocalViewModelStoreOwner provides viewModelStoreOwner) {
                AlbumScreen(localPlaylistViewModel.albumInfoState)
            }
        }

        composable<Destination.SavedPlaylists>(
            typeMap = mapOf(typeOf<Album>() to AlbumType),
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up,
                    initialOffset = { it / 4 }) + scaleIn(initialScale = 3f / 4) + fadeIn()
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down,
                    targetOffset = { it / 4 }) + scaleOut(targetScale = 3f / 4) + fadeOut()
            }
        ) {
            val playlistInfoViewModel: PlaylistInfoViewModel =
                viewModel(factory = PlaylistInfoViewModel.Factory)
            CompositionLocalProvider(LocalViewModelStoreOwner provides viewModelStoreOwner) {
                AlbumScreen(playlistInfoViewModel.albumInfoState)
            }
        }

        composable<Destination.OnlineArtist>(
            typeMap = mapOf(typeOf<Artist>() to ArtistType),
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up,
                    initialOffset = { it / 4 }) + scaleIn(initialScale = 3f / 4) + fadeIn()
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down,
                    targetOffset = { it / 4 }) + scaleOut(targetScale = 3f / 4) + fadeOut()
            }
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
            typeMap = mapOf(typeOf<Artist>() to ArtistType),
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up,
                    initialOffset = { it / 4 }) + scaleIn(initialScale = 3f / 4) + fadeIn()
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down,
                    targetOffset = { it / 4 }) + scaleOut(targetScale = 3f / 4) + fadeOut()
            }
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
