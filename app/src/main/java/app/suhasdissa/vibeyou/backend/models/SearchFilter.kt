package app.suhasdissa.vibeyou.backend.models

enum class SearchFilter(val value: String) {
    Songs("music_songs"),
    Videos("music_videos"),
    Albums("music_albums"),
    Playlists("music_playlists"),
    Artists("music_artists")
}

enum class LocalSearchFilter {
    Songs,
    Albums,
    Artists
}
