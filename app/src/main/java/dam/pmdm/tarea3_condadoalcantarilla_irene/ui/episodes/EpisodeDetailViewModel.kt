package dam.pmdm.tarea3_condadoalcantarilla_irene.ui.episodes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dam.pmdm.tarea3_condadoalcantarilla_irene.data.model.Character
import dam.pmdm.tarea3_condadoalcantarilla_irene.data.model.Episode
import dam.pmdm.tarea3_condadoalcantarilla_irene.data.repository.EpisodeRepository
import kotlinx.coroutines.launch

class EpisodeDetailViewModel : ViewModel() {
    
    private val repository = EpisodeRepository()
    
    private val _episode = MutableLiveData<Episode>()
    val episode: LiveData<Episode> = _episode
    
    private val _characters = MutableLiveData<List<Character>>()
    val characters: LiveData<List<Character>> = _characters
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _isLoadingCharacters = MutableLiveData<Boolean>()
    val isLoadingCharacters: LiveData<Boolean> = _isLoadingCharacters
    
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error
    
    fun loadEpisode(episodeId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            try {
                val episode = repository.getEpisode(episodeId)
                if (episode != null) {
                    _episode.value = episode
                    loadCharacters(episode.characters)
                } else {
                    _error.value = "Episode not found"
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    private fun loadCharacters(characterUrls: List<String>) {
        viewModelScope.launch {
            _isLoadingCharacters.value = true
            
            try {
                val characters = repository.getCharacters(characterUrls)
                _characters.value = characters
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoadingCharacters.value = false
            }
        }
    }
    
    fun toggleWatchedStatus(watched: Boolean) {
        val currentEpisode = _episode.value ?: return
        
        viewModelScope.launch {
            try {
                repository.markEpisodeAsWatched(currentEpisode, watched)
                currentEpisode.isWatched = watched
                _episode.value = currentEpisode
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}
