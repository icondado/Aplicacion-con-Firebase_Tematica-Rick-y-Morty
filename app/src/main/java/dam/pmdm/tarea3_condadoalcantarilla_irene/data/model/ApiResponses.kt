package dam.pmdm.tarea3_condadoalcantarilla_irene.data.model

import com.google.gson.annotations.SerializedName

data class EpisodeResponse(
    @SerializedName("info")
    val info: Info,
    
    @SerializedName("results")
    val results: List<Episode>
)

data class CharacterResponse(
    @SerializedName("info")
    val info: Info,

    @SerializedName("results")
    val results: List<Character>,
)

data class Info(
    @SerializedName("count")
    val count: Int,
    
    @SerializedName("pages")
    val pages: Int,
    
    @SerializedName("next")
    val next: String?,
    
    @SerializedName("prev")
    val prev: String?
)
