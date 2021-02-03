package com.example.accenturechallenge.ui.pokemons.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.accenturechallenge.R
import com.example.accenturechallenge.data.database.entities.DbPokemon
import com.example.accenturechallenge.databinding.ListItemPokemonBinding
import com.example.accenturechallenge.utils.POKEMON_IMAGE_BASE_URL
import com.example.accenturechallenge.utils.loadImage

class PokemonListPagingDataAdapter:  PagingDataAdapter<DbPokemon, PokemonListPagingDataAdapter.PokemonViewHolder>(PokemonListDiffCallBack()) {

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        return PokemonViewHolder.from(parent)
    }

    class PokemonViewHolder private constructor(
        private val binding: ListItemPokemonBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        //TODO remove this should be done on the APIMapper
        private val regex = "(\\d+)(?!.*\\d)".toRegex()

        companion object {
            fun from(parent: ViewGroup): PokemonViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemPokemonBinding.inflate(layoutInflater, parent, false)
                return PokemonViewHolder(
                    binding
                )
            }
        }

        fun bind(item: DbPokemon) {
            with(binding) {
                pokemonName.text = item.name

                val pokemonId = regex.find(item.url)?.value ?: ""
                pokemonImage.loadImage("$POKEMON_IMAGE_BASE_URL$pokemonId.png", R.drawable.ic_baseline_emoji_emotions_24)
            }

        }

    }
}

private class PokemonListDiffCallBack : DiffUtil.ItemCallback<DbPokemon>() {
    override fun areContentsTheSame(oldItem: DbPokemon, newItem: DbPokemon): Boolean =
        oldItem.name == newItem.name

    override fun areItemsTheSame(oldItem: DbPokemon, newItem: DbPokemon): Boolean =
        oldItem == newItem
}