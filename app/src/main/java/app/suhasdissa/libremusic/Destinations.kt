package app.suhasdissa.libremusic

interface Destinations {
    val route: String
}

object Home : Destinations {
    override val route = "home"
}

object Search : Destinations {
    override val route = "search"
}

object Settings : Destinations {
    override val route = "settings"
}

object About : Destinations {
    override val route = "about"
}