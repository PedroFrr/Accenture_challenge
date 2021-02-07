package com.example.accenturechallenge.ui.pokemons.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.accenturechallenge.R
import com.example.accenturechallenge.core.PokemonUiModel
import com.example.accenturechallenge.data.database.entities.DbPokemonWithOrWithoutFavorites
import com.example.accenturechallenge.databinding.ListItemPokemonBinding
import com.example.accenturechallenge.utils.loadImage

class PokemonListPagingDataAdapter(
    private val favoritePokemon: (pokemonWithOrWithout: DbPokemonWithOrWithoutFavorites) -> Unit
) : PagingDataAdapter<PokemonUiModel, RecyclerView.ViewHolder>(
    PokemonListDiffCallBack()
) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val pokemonUiModel = getItem(position)
        pokemonUiModel.let {
            when (it) {
                is PokemonUiModel.PokemonItem -> (holder as PokemonViewHolder).bind(it.pokemonResult, favoritePokemon)
                is PokemonUiModel.SeparatorItem -> (holder as PokemonSeparatorViewHolder).bind(it.description)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is PokemonUiModel.PokemonItem -> R.layout.list_item_pokemon
            is PokemonUiModel.SeparatorItem -> R.layout.separator_pokemon_view_item
            null -> throw UnsupportedOperationException("Unknown view")
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == R.layout.list_item_pokemon) {
            PokemonViewHolder.from(parent)
        } else {
            PokemonSeparatorViewHolder.from(parent)
        }
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

private class PokemonListDiffCallBack : DiffUtil.ItemCallback<PokemonUiModel>() {

    override fun areItemsTheSame(oldItem: PokemonUiModel, newItem: PokemonUiModel): Boolean {
        return (oldItem is PokemonUiModel.PokemonItem && newItem is PokemonUiModel.PokemonItem &&
                oldItem.pokemonResult.pokemon.id == newItem.pokemonResult.pokemon.id) ||
                (oldItem is PokemonUiModel.SeparatorItem && newItem is PokemonUiModel.SeparatorItem &&
                        oldItem.description == newItem.description)
    }


    override fun areContentsTheSame(oldItem: PokemonUiModel, newItem: PokemonUiModel): Boolean =
        oldItem == newItem
}
