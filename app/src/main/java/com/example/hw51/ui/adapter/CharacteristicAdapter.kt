package com.example.hw51.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.hw51.interfaces.OnClickItem
import com.example.hw51.R
import com.example.hw51.data.model.Character
import com.example.hw51.databinding.ItemCharacteristicBinding

class CharacteristicAdapter(private val onClick: OnClickItem) :
    ListAdapter<Character, CharacteristicAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(val binding: ItemCharacteristicBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(character: Character) = with(binding) {
            tvName.text = character.name
            tvLastKnowLocation.text = character.location.name
            tvFirstSeenIn.text = character.origin.name
            igCharacter.load(character.image) {
                crossfade(true)
            }
            tvStatus.text = character.status
            imgColorIndicator.setImageResource(
                when {
                    character.status.contains("Dead") -> R.drawable.ic_circle_red
                    character.status.contains("Alive") -> R.drawable.ic_circle_green
                    else -> R.drawable.ic_cicrle_gray
                }
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCharacteristicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(getItem(position))
        holder.itemView.setOnClickListener {
            onClick.onClick(getItem(position))
        }
    }
}

private val diffUtil = object : DiffUtil.ItemCallback<Character>() {
    override fun areItemsTheSame(oldItem: Character, newItem: Character) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Character, newItem: Character) = oldItem == newItem
}