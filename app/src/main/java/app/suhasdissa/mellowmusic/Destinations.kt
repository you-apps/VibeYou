package app.suhasdissa.mellowmusic

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

object NetworkSettings : Destinations {
    override val route = "net_settings"
}

object DatabaseSettings : Destinations {
    override val route = "database_settings"
}

object Songs : Destinations {
    override val route = "songs"
}

object FavouriteSongs : Destinations {
    override val route = "favsongs"
}
