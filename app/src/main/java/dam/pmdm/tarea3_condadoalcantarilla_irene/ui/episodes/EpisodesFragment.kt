package dam.pmdm.tarea3_condadoalcantarilla_irene.ui.episodes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dam.pmdm.tarea3_condadoalcantarilla_irene.R
import dam.pmdm.tarea3_condadoalcantarilla_irene.databinding.FragmentEpisodesBinding

class EpisodesFragment : Fragment() {
    
    private var _binding: FragmentEpisodesBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: EpisodesViewModel by viewModels()
    private lateinit var adapter: EpisodesAdapter
    
    private var isSelectionMode = false
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEpisodesBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        setupFilterChips()
        setupObservers()
        setupMultipleSelection()
    }
    
    private fun setupRecyclerView() {
        adapter = EpisodesAdapter { episode ->
            // Navigate to episode detail
            val action = EpisodesFragmentDirections
                .actionEpisodesToDetail(episode.id)
            findNavController().navigate(action)
        }
        
        binding.recyclerViewEpisodes.adapter = adapter
    }

    private fun setupFilterChips() {
        binding.chipGroupFilter.setOnCheckedStateChangeListener { _, checkedIds ->
            when {
                checkedIds.contains(R.id.chipAll) -> {
                    viewModel.setFilter(EpisodesViewModel.FilterType.ALL)
                }
                checkedIds.contains(R.id.chipWatched) -> {
                    viewModel.setFilter(EpisodesViewModel.FilterType.WATCHED)
                }
            }
        }
    }


    private fun setupMultipleSelection() {
        binding.fabMultipleSelection.setOnClickListener {
            isSelectionMode = !isSelectionMode
            adapter.setSelectionMode(isSelectionMode)
            
            if (isSelectionMode) {
                binding.cardSelectionActions.visibility = View.VISIBLE
                binding.fabMultipleSelection.text = getString(R.string.cancel_selection)
                updateSelectionCount()
            } else {
                binding.cardSelectionActions.visibility = View.GONE
                binding.fabMultipleSelection.text = getString(R.string.select_multiple)
            }
        }
        
        binding.btnMarkSelectedWatched.setOnClickListener {
            val selectedEpisodes = adapter.getSelectedEpisodes()
            if (selectedEpisodes.isNotEmpty()) {
                viewModel.markMultipleEpisodesAsWatched(selectedEpisodes)
                Toast.makeText(
                    requireContext(),
                    "${selectedEpisodes.size} episodios marcados como vistos",
                    Toast.LENGTH_SHORT
                ).show()
                exitSelectionMode()
            }
        }
        
        binding.btnCancelSelection.setOnClickListener {
            exitSelectionMode()
        }
    }
    
    private fun exitSelectionMode() {
        isSelectionMode = false
        adapter.setSelectionMode(false)
        binding.cardSelectionActions.visibility = View.GONE
        binding.fabMultipleSelection.text = getString(R.string.select_multiple)
    }
    
    private fun updateSelectionCount() {
        val count = adapter.getSelectionCount()
        binding.tvSelectedCount.text = getString(R.string.selected_count, count)
    }
    
    private fun setupObservers() {
        viewModel.filteredEpisodes.observe(viewLifecycleOwner) { episodes ->
            adapter.submitList(episodes)
            binding.tvEmptyState.visibility = if (episodes.isEmpty()) {
                View.VISIBLE
            } else {
                View.GONE
            }
            
            if (isSelectionMode) {
                updateSelectionCount()
            }
        }
        
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) {
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
