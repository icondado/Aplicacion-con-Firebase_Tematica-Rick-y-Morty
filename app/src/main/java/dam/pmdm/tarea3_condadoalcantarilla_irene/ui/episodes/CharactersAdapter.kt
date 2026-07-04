package dam.pmdm.tarea3_condadoalcantarilla_ireneui.episodes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dam.pmdm.tarea3_condadoalcantarilla_irene.data.model.Character
import dam.pmdm.tarea3_condadoalcantarilla_irene.databinding.ItemCharacterBinding
import com.bumptech.glide.Glide

class CharactersAdapter : ListAdapter<Character, CharactersAdapter.CharacterViewHolder>(CharacterDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val binding = ItemCharacterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CharacterViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    class CharacterViewHolder(
        private val binding: ItemCharacterBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(character: Character) {
            binding.tvCharacterName.text = character.name
            
            Glide.with(binding.root.context)
                .load(character.image)
                .centerCrop()
                .into(binding.ivCharacter)
        }
    }
    
    class CharacterDiffCallback : DiffUtil.ItemCallback<Character>() {
        override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem == newItem
        }
    }
}
