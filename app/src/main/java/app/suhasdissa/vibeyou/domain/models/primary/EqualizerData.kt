package app.suhasdissa.vibeyou.domain.models.primary

data class EqualizerBand(
    val frequency: Int,
    val minLevel: Short,
    val maxLevel: Short,
)

data class EqualizerData(
    val presets: List<String>,
    val bands: List<EqualizerBand>
)
