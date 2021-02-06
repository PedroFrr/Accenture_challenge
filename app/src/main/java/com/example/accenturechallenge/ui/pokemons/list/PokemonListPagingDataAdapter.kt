package com.example.accenturechallenge.ui.pokemons.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.accenturechallenge.R
import com.example.accenturechallenge.data.database.entities.DbPokemonWithOrWithoutFavorites
import com.example.accenturechallenge.databinding.ListItemPokemonBinding
import com.example.accenturechallenge.utils.loadImage

class PokemonListPagingDataAdapter(
    private val favoritePokemon: (pokemonWithOrWithout: DbPokemonWithOrWithoutFavorites) -> Unit
) : PagingDataAdapter<DbPokemonWithOrWithoutFavorites, PokemonListPagingDataAdapter.PokemonViewHolder>(
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
            item: DbPokemonWithOrWithoutFavorites,
            favoritePokemon: (pokemonWithOrWithout: DbPokemonWithOrWithoutFavorites) -> Unit
        ) {
            with(binding) {
                pokemonName.text = item.pokemon.name
                //Sets favorite icon based on its condition
                //If the Pokemon is not a favorite it means its favorite value is null
                val favoriteDrawable = if (item.favorite != null) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_favorite_border_24
                isFavourite.setImageResource(favoriteDrawable)
                pokemonImage.loadImage(item.pokemon.url)

                isFavourite.setOnClickListener {
                    favoritePokemon(item)
                }

                pokemonCard.setOnClickListener {
                    val directions = PokemonListFragmentDirections.pokemonListToDetail(item.pokemon.id )
                    Navigation.findNavController(it).navigate(directions)
                }

            }

        }

    }
}

private class PokemonListDiffCallBack : DiffUtil.ItemCallback<DbPokemonWithOrWithoutFavorites>() {
    override fun areContentsTheSame(oldItem: DbPokemonWithOrWithoutFavorites, newItem: DbPokemonWithOrWithoutFavorites): Boolean =
        oldItem.pokemon.id == newItem.pokemon.id

    override fun areItemsTheSame(oldItem: DbPokemonWithOrWithoutFavorites, newItem: DbPokemonWithOrWithoutFavorites): Boolean =
        oldItem == newItem
}
