package com.example.accenturechallenge.ui.pokemons.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.accenturechallenge.R
import com.example.accenturechallenge.data.database.entities.DbPokemon
import com.example.accenturechallenge.databinding.ListItemPokemonBinding
import com.example.accenturechallenge.utils.loadImage

class PokemonListPagingDataAdapter(
    private val favoritePokemon: (pokemon: DbPokemon) -> Unit
) : PagingDataAdapter<DbPokemon, PokemonListPagingDataAdapter.PokemonViewHolder>(
    PokemonListDiffCallBack()
) {

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it, favoritePokemon)
        }
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

        fun bind(
            item: DbPokemon,
            favoritePokemon: (pokemon: DbPokemon) -> Unit
        ) {
            with(binding) {
                pokemonName.text = item.name
                //Sets favorite icon based on its condition
                val favoriteDrawable =
                    if (item.isFavorite) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_favorite_border_24
                isFavourite.setImageResource(favoriteDrawable)
                pokemonImage.loadImage(item.url)

                isFavourite.setOnClickListener {
                    favoritePokemon(item)
                }

                pokemonCard.setOnClickListener {
                    val directions = PokemonListFragmentDirections.pokemonListToDetail(item.id)
                    Navigation.findNavController(it).navigate(directions)
                }

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