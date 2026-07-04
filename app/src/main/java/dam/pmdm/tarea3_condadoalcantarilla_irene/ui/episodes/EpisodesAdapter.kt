package dam.pmdm.tarea3_condadoalcantarilla_irene.ui.episodes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dam.pmdm.tarea3_condadoalcantarilla_irene.R
import dam.pmdm.tarea3_condadoalcantarilla_irene.data.model.Episode
import dam.pmdm.tarea3_condadoalcantarilla_irene.databinding.ItemEpisodeBinding

class EpisodesAdapter(
    private val onEpisodeClick: (Episode) -> Unit
) : ListAdapter<Episode, EpisodesAdapter.EpisodeViewHolder>(EpisodeDiffCallback()) {
    
    private var isSelectionMode = false
    private val selectedEpisodes = mutableSetOf<Episode>()
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        val binding = ItemEpisodeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EpisodeViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    fun setSelectionMode(enabled: Boolean) {
        isSelectionMode = enabled
        if (!enabled) {
            selectedEpisodes.clear()
        }
        notifyDataSetChanged()
    }
    
    fun getSelectedEpisodes(): List<Episode> = selectedEpisodes.toList()
    
    fun getSelectionCount(): Int = selectedEpisodes.size
    
    inner class EpisodeViewHolder(
        private val binding: ItemEpisodeBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(episode: Episode) {
            binding.apply {
                tvEpisodeCode.text = episode.episode
                tvEpisodeName.text = episode.name
                tvAirDate.text = episode.airDate
                
                // Watched indicator
                val indicatorColor = if (episode.isWatched) {
                    R.color.episode_watched
                } else {
                    R.color.episode_not_watched
                }
                viewWatchedIndicator.setBackgroundColor(
                    ContextCompat.getColor(root.context, indicatorColor)
                )
                
                // Selection mode
                if (isSelectionMode) {
                    checkboxSelected.visibility = View.VISIBLE
                    checkboxSelected.isChecked = selectedEpisodes.contains(episode)
                    
                    // Apply selection overlay
                    if (selectedEpisodes.contains(episode)) {
                        layoutEpisode.setBackgroundColor(
                            ContextCompat.getColor(root.context, R.color.selection_overlay)
                        )
                    } else {
                        layoutEpisode.setBackgroundColor(
                            ContextCompat.getColor(root.context, android.R.color.transparent)
                        )
                    }
                } else {
                    checkboxSelected.visibility = View.GONE
                    layoutEpisode.setBackgroundColor(
                        ContextCompat.getColor(root.context, android.R.color.transparent)
                    )
                }
                
                // Click listeners
                root.setOnClickListener {
                    if (isSelectionMode) {
                        toggleSelection(episode)
                    } else {
                        onEpisodeClick(episode)
                    }
                }
                
                checkboxSelected.setOnClickListener {
                    toggleSelection(episode)
                }
            }
        }
        
        private fun toggleSelection(episode: Episode) {
            if (selectedEpisodes.contains(episode)) {
                selectedEpisodes.remove(episode)
            } else {
                selectedEpisodes.add(episode)
            }
            notifyItemChanged(bindingAdapterPosition)
        }
    }
    
    class EpisodeDiffCallback : DiffUtil.ItemCallback<Episode>() {
        override fun areItemsTheSame(oldItem: Episode, newItem: Episode): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: Episode, newItem: Episode): Boolean {
            return oldItem == newItem
        }
    }
}
