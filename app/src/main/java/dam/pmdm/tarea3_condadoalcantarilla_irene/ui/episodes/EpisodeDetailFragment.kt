package dam.pmdm.tarea3_condadoalcantarilla_irene.ui.episodes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dam.pmdm.tarea3_condadoalcantarilla_irene.R
import dam.pmdm.tarea3_condadoalcantarilla_irene.databinding.FragmentEpisodeDetailBinding
import dam.pmdm.tarea3_condadoalcantarilla_ireneui.episodes.CharactersAdapter

class EpisodeDetailFragment : Fragment() {
    
    private var _binding: FragmentEpisodeDetailBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: EpisodeDetailViewModel by viewModels()
    private val args: EpisodeDetailFragmentArgs by navArgs()
    
    private lateinit var charactersAdapter: CharactersAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEpisodeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        setupObservers()
        setupListeners()
        
        viewModel.loadEpisode(args.episodeId)
    }
    
    private fun setupRecyclerView() {
        charactersAdapter = CharactersAdapter()
        binding.recyclerViewCharacters.adapter = charactersAdapter
    }
    
    private fun setupListeners() {
        binding.switchWatched.setOnCheckedChangeListener { _, isChecked ->
            viewModel.toggleWatchedStatus(isChecked)
        }
    }
    
    private fun setupObservers() {
        viewModel.episode.observe(viewLifecycleOwner) { episode ->
            binding.apply {
                tvEpisodeCode.text = episode.episode
                tvEpisodeName.text = episode.name
                tvAirDate.text = getString(R.string.air_date, episode.airDate)
                
                // Set switch without triggering listener
                switchWatched.setOnCheckedChangeListener(null)
                switchWatched.isChecked = episode.isWatched
                switchWatched.setOnCheckedChangeListener { _, isChecked ->
                    viewModel.toggleWatchedStatus(isChecked)
                }
            }
        }
        
        viewModel.characters.observe(viewLifecycleOwner) { characters ->
            charactersAdapter.submitList(characters)
        }
        
        viewModel.isLoadingCharacters.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBarCharacters.visibility = if (isLoading) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
        
        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
