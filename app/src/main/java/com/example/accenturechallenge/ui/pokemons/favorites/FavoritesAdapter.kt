package com.example.accenturechallenge.ui.pokemons.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.accenturechallenge.R
import com.example.accenturechallenge.data.database.entities.DbPokemon
import com.example.accenturechallenge.data.database.entities.DbPokemonWithFavorites
import com.example.accenturechallenge.data.database.entities.DbPokemonWithOrWithoutFavorites
import com.example.accenturechallenge.databinding.ListItemPokemonBinding
import com.example.accenturechallenge.utils.loadImage

class FavoritesAdapter(private val onFavoritePokemon: (pokemon: DbPokemonWithOrWithoutFavorites) -> Unit) : ListAdapter<DbPokemonWithOrWithoutFavorites, FavoritesAdapter.PokemonViewHolder>(
    PokemonListDiffCallBack()
) {

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it, onFavoritePokemon)
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
            onFavoritePokemon: (pokemonWithFavorites: DbPokemonWithOrWithoutFavorites) -> Unit
        ) {
            with(binding) {
                pokemonName.text = item.pokemon.name
                //Here all the Pokemons are favorite
                isFavourite.setImageResource(R.drawable.ic_baseline_favorite_24)
                isFavourite.setOnClickListener {
                    onFavoritePokemon(item)
                }
                pokemonImage.loadImage(item.pokemon.url)

                pokemonCard.setOnClickListener {
                    val directions = FavoritesFragmentDirections.favoriteListoDetail(item.pokemon.id)
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