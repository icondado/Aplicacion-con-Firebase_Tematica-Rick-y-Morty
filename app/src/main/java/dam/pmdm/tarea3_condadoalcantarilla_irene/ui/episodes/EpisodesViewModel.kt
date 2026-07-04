package dam.pmdm.tarea3_condadoalcantarilla_irene.ui.episodes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dam.pmdm.tarea3_condadoalcantarilla_irene.data.model.Episode
import dam.pmdm.tarea3_condadoalcantarilla_irene.data.repository.EpisodeRepository
import kotlinx.coroutines.launch

class EpisodesViewModel : ViewModel() {
    
    private val repository = EpisodeRepository()
    
    private val _episodes = MutableLiveData<List<Episode>>()
    val episodes: LiveData<List<Episode>> = _episodes
    
    private val _filteredEpisodes = MutableLiveData<List<Episode>>()
    val filteredEpisodes: LiveData<List<Episode>> = _filteredEpisodes
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error
    
    private var currentFilter = FilterType.ALL
    
    enum class FilterType {
        ALL, WATCHED
    }
    
    init {
        loadEpisodes()
    }
    
    fun loadEpisodes() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            try {
                val episodesList = repository.getAllEpisodes()
                _episodes.value = episodesList
                applyFilter(currentFilter)
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun setFilter(filterType: FilterType) {
        currentFilter = filterType
        applyFilter(filterType)
    }
    
    private fun applyFilter(filterType: FilterType) {
        val episodesList = _episodes.value ?: return
        
        _filteredEpisodes.value = when (filterType) {
            FilterType.ALL -> episodesList
            FilterType.WATCHED -> episodesList.filter { it.isWatched }
        }
    }
    
    fun markEpisodeAsWatched(episode: Episode, watched: Boolean) {
        viewModelScope.launch {
            try {
                repository.markEpisodeAsWatched(episode, watched)
                // Refresh the current filter
                applyFilter(currentFilter)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
    
    fun markMultipleEpisodesAsWatched(episodes: List<Episode>) {
        viewModelScope.launch {
            try {
                episodes.forEach { episode ->
                    repository.markEpisodeAsWatched(episode, true)
                }
                // Reload episodes to reflect changes
                loadEpisodes()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}
