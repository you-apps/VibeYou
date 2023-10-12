package app.suhasdissa.vibeyou.backend.models

import app.suhasdissa.vibeyou.backend.models.songs.SongItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PipedSongResponse(
    @SerialName("title") var title: String? = null,
    @SerialName("description") var description: String? = null,
    @SerialName("uploader") var uploader: String? = null,
    @SerialName("thumbnailUrl") var thumbnailUrl: String? = null,
    @SerialName("hls") var hls: String? = null,
    @SerialName("duration") var duration: Int? = null,
    @SerialName("audioStreams") var audioStreams: ArrayList<AudioStreams> = arrayListOf(),
    @SerialName("relatedStreams") var relatedStreams: ArrayList<SongItem> = arrayListOf()

)

@Serializable
data class AudioStreams(

    @SerialName("url") var url: String? = null
    /* @SerialName("format") var format: String? = null,
     @SerialName("quality") var quality: String? = null,
     @SerialName("mimeType") var mimeType: String? = null,
     @SerialName("codec") var codec: String? = null,
     @SerialName("audioTrackId") var audioTrackId: String? = null,
     @SerialName("audioTrackName") var audioTrackName: String? = null,
     @SerialName("videoOnly") var videoOnly: Boolean? = null,
     @SerialName("bitrate") var bitrate: Int? = null,
     @SerialName("initStart") var initStart: Int? = null,
     @SerialName("initEnd") var initEnd: Int? = null,
     @SerialName("indexStart") var indexStart: Int? = null,
     @SerialName("indexEnd") var indexEnd: Int? = null,
     @SerialName("width") var width: Int? = null,
     @SerialName("height") var height: Int? = null,
     @SerialName("fps") var fps: Int? = null*/

)
