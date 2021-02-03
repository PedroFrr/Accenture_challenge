package com.example.accenturechallenge.ui.pokemons.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.accenturechallenge.data.network.response.PokemonResult
import com.example.accenturechallenge.databinding.ListItemPokemonBinding

class PokemonListPagingDataAdapter:  PagingDataAdapter<PokemonResult, PokemonListPagingDataAdapter.PokemonViewHolder>(PokemonListDiffCallBack()) {

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        return PokemonViewHolder.from(parent)
    }

    class PokemonViewHolder private constructor(
        private val binding: ListItemPokemonBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): PokemonViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemPokemonBinding.inflate(layoutInflater, parent, false)
                return PokemonViewHolder(
                    binding
                )
            }
        }

        fun bind(item: PokemonResult) {
            with(binding) {
                pokemoName.text = item.name
            }

        }

    }
}

private class PokemonListDiffCallBack : DiffUtil.ItemCallback<PokemonResult>() {
    override fun areContentsTheSame(oldItem: PokemonResult, newItem: PokemonResult): Boolean =
        oldItem.name == newItem.name

    override fun areItemsTheSame(oldItem: PokemonResult, newItem: PokemonResult): Boolean =
        oldItem == newItem
}