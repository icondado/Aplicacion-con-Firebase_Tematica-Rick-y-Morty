package dam.pmdm.tarea3_condadoalcantarilla_irene.data.model

data class WatchedEpisode(
    val episodeId: Int = 0,
    val name: String = "",
    val episode: String = "",
    val airDate: String = "",
    val characters: List<String> = emptyList(),
    val viewed: Boolean = true,
    val timestamp: Long = System.currentTimeMillis()
)
