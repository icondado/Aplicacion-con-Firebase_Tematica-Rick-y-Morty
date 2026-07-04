package dam.pmdm.tarea3_condadoalcantarilla_irene.ui.statistics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dam.pmdm.tarea3_condadoalcantarilla_irene.data.repository.EpisodeRepository
import kotlinx.coroutines.launch

class StatisticsViewModel : ViewModel() {
    
    private val repository = EpisodeRepository()
    
    private val _watchedCount = MutableLiveData<Int>()
    val watchedCount: LiveData<Int> = _watchedCount
    
    private val _totalCount = MutableLiveData<Int>()
    val totalCount: LiveData<Int> = _totalCount
    
    private val _percentage = MutableLiveData<Float>()
    val percentage: LiveData<Float> = _percentage
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    fun loadStatistics() {
        viewModelScope.launch {
            _isLoading.value = true
            
            try {
                val allEpisodes = repository.getAllEpisodes()
                val watchedEpisodes = allEpisodes.count { it.isWatched }
                val totalEpisodes = allEpisodes.size
                
                _watchedCount.value = watchedEpisodes
                _totalCount.value = totalEpisodes
                _percentage.value = if (totalEpisodes > 0) {
                    (watchedEpisodes.toFloat() / totalEpisodes.toFloat()) * 100f
                } else {
                    0f
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
