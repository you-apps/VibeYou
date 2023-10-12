package app.suhasdissa.vibeyou

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.net.toUri
import androidx.navigation.compose.rememberNavController
import app.suhasdissa.vibeyou.backend.viewmodel.PlayerViewModel
import app.suhasdissa.vibeyou.ui.components.NavDrawerContent
import app.suhasdissa.vibeyou.ui.theme.LibreMusicTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val playerViewModel: PlayerViewModel by viewModels { PlayerViewModel.Factory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LibreMusicTheme {
                val navHostController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    val drawerState = rememberDrawerState(DrawerValue.Closed)
                    val scope = rememberCoroutineScope()
                    var currentDestination by remember {
                        mutableStateOf<Destination>(Destination.PipedMusic)
                    }
                    ModalNavigationDrawer(
                        drawerState = drawerState,
                        gesturesEnabled = drawerState.isOpen,
                        drawerContent = {
                            NavDrawerContent(
                                currentDestination = currentDestination,
                                onDestinationSelected = {
                                    scope.launch {
                                        drawerState.close()
                                    }
                                    navHostController.navigateTo(it.route)
                                    currentDestination = it
                                }
                            )
                        }
                    ) {
                        AppNavHost(navHostController = navHostController)
                    }
                }
            }
        }

        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        handleIntent(intent)
        super.onNewIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        when (intent.action) {
            Intent.ACTION_VIEW -> {
                val uri = intent.data
                uri?.let {
                    processLink(uri)
                }
            }

            Intent.ACTION_SEND -> {
                intent.getStringExtra(Intent.EXTRA_TEXT)
                    ?.let { sharedContent ->
                        intent.removeExtra(Intent.EXTRA_TEXT)
                        Log.e("IntentHandler", sharedContent)
                        val pattern = Regex("https://youtu.be/[\\S]+")
                        if (pattern.containsMatchIn(sharedContent)) {
                            kotlin.runCatching {
                                val uri = sharedContent.toUri()
                                processLink(uri)
                            }
                        }
                    }
            }
        }
    }

    private fun processLink(uri: Uri) {
        Toast.makeText(this, "Opening url...", Toast.LENGTH_SHORT).show()
        val path = uri.pathSegments.firstOrNull()

        val vidId = if (path == "watch") {
            uri.getQueryParameter("v")
        } else if (uri.host == "youtu.be") {
            path
        } else {
            null
        }

        if (vidId == null) {
            Toast.makeText(this, "Invalid Url", Toast.LENGTH_SHORT).show()
            return
        }
        Toast.makeText(this, vidId, Toast.LENGTH_SHORT).show()
        playerViewModel.tryToPlayId(vidId)
    }
}
