package dam.pmdm.tarea3_condadoalcantarilla_irene.data.repository

import dam.pmdm.tarea3_condadoalcantarilla_irene.data.api.RetrofitClient
import dam.pmdm.tarea3_condadoalcantarilla_irene.data.model.Character
import dam.pmdm.tarea3_condadoalcantarilla_irene.data.model.Episode
import dam.pmdm.tarea3_condadoalcantarilla_irene.data.model.WatchedEpisode
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class EpisodeRepository {
    
    private val apiService = RetrofitClient.apiService
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    
    // Get all episodes from API
    suspend fun getAllEpisodes(): List<Episode> {
        val allEpisodes = mutableListOf<Episode>()
        var page = 1
        var hasNextPage = true
        
        while (hasNextPage) {
            val response = apiService.getAllEpisodes(page)
            if (response.isSuccessful) {
                response.body()?.let { episodeResponse ->
                    allEpisodes.addAll(episodeResponse.results)
                    hasNextPage = episodeResponse.info.next != null
                    page++
                }
            } else {
                hasNextPage = false
            }
        }
        
        // Load watched status from Firestore
        loadWatchedStatus(allEpisodes)
        
        return allEpisodes
    }
    
    // Get single episode from API
    suspend fun getEpisode(id: Int): Episode? {
        val response = apiService.getEpisode(id)
        return if (response.isSuccessful) {
            response.body()?.also { episode ->
                episode.isWatched = isEpisodeWatched(id)
            }
        } else {
            null
        }
    }
    
    // Get character from API
    suspend fun getCharacter(id: Int): Character? {
        val response = apiService.getCharacter(id)
        return if (response.isSuccessful) response.body() else null
    }
    
    // Get multiple characters from API
    suspend fun getCharacters(characterUrls: List<String>): List<Character> {
        val characterIds = characterUrls.mapNotNull { url ->
            url.substringAfterLast("/").toIntOrNull()
        }
        
        if (characterIds.isEmpty()) return emptyList()
        
        val idsString = characterIds.joinToString(",")
        val response = apiService.getMultipleCharacters(idsString)
        
        return if (response.isSuccessful) {
            response.body() ?: emptyList()
        } else {
            emptyList()
        }
    }
    
    // Mark episode as watched in Firestore
    suspend fun markEpisodeAsWatched(episode: Episode, watched: Boolean) {
        val userId = auth.currentUser?.uid ?: return
        
        val watchedEpisode = WatchedEpisode(
            episodeId = episode.id,
            name = episode.name,
            episode = episode.episode,
            airDate = episode.airDate,
            characters = episode.characters,
            viewed = watched
        )
        
        if (watched) {
            firestore.collection("users")
                .document(userId)
                .collection("watched_episodes")
                .document(episode.id.toString())
                .set(watchedEpisode)
                .await()
        } else {
            firestore.collection("users")
                .document(userId)
                .collection("watched_episodes")
                .document(episode.id.toString())
                .delete()
                .await()
        }
        
        episode.isWatched = watched
    }
    
    // Load watched status from Firestore
    private suspend fun loadWatchedStatus(episodes: List<Episode>) {
        val userId = auth.currentUser?.uid ?: return
        
        try {
            val watchedEpisodes = firestore.collection("users")
                .document(userId)
                .collection("watched_episodes")
                .get()
                .await()
            
            val watchedIds = watchedEpisodes.documents.mapNotNull { 
                it.getLong("episodeId")?.toInt() 
            }.toSet()
            
            episodes.forEach { episode ->
                episode.isWatched = watchedIds.contains(episode.id)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    // Check if single episode is watched
    private suspend fun isEpisodeWatched(episodeId: Int): Boolean {
        val userId = auth.currentUser?.uid ?: return false
        
        return try {
            val document = firestore.collection("users")
                .document(userId)
                .collection("watched_episodes")
                .document(episodeId.toString())
                .get()
                .await()
            
            document.exists()
        } catch (e: Exception) {
            false
        }
    }
    
    // Get watched episodes count
    suspend fun getWatchedEpisodesCount(): Int {
        val userId = auth.currentUser?.uid ?: return 0
        
        return try {
            val watchedEpisodes = firestore.collection("users")
                .document(userId)
                .collection("watched_episodes")
                .get()
                .await()
            
            watchedEpisodes.size()
        } catch (e: Exception) {
            0
        }
    }
}
