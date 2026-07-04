package dam.pmdm.tarea3_condadoalcantarilla_irene.ui.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import dam.pmdm.tarea3_condadoalcantarilla_irene.R
import dam.pmdm.tarea3_condadoalcantarilla_irene.databinding.FragmentStatisticsBinding
import dam.pmdm.tarea3_condadoalcantarilla_irene.data.repository.EpisodeRepository
import kotlinx.coroutines.launch

class StatisticsFragment : Fragment() {

    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!

    private val repository = EpisodeRepository()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadStatistics()
    }

    private fun loadStatistics() {
        lifecycleScope.launch {
            binding.progressBarLoading.visibility = View.VISIBLE

            val watchedCount = repository.getWatchedEpisodesCount()
            val totalEpisodes = 51
            val percentage = (watchedCount * 100) / totalEpisodes

            binding.tvWatchedCount.text = getString(R.string.watched_count, watchedCount, totalEpisodes)
            binding.tvPercentage.text = "$percentage%"
            binding.progressBarStats.progress = percentage

            binding.progressBarLoading.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}