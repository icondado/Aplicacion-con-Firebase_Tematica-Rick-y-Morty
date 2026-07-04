package dam.pmdm.tarea3_condadoalcantarilla_irene.data.api

import dam.pmdm.tarea3_condadoalcantarilla_irene.data.model.Character
import dam.pmdm.tarea3_condadoalcantarilla_irene.data.model.Episode
import dam.pmdm.tarea3_condadoalcantarilla_irene.data.model.EpisodeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AppTarea3 {
    
    @GET("episode")
    suspend fun getAllEpisodes(@Query("page") page: Int = 1): Response<EpisodeResponse>
    
    @GET("episode/{id}")
    suspend fun getEpisode(@Path("id") id: Int): Response<Episode>
    
    @GET("character/{id}")
    suspend fun getCharacter(@Path("id") id: Int): Response<Character>
    
    @GET("character/{ids}")
    suspend fun getMultipleCharacters(@Path("ids") ids: String): Response<List<Character>>
}
