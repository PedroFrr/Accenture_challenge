package com.example.accenturechallenge.ui.pokemons.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import com.example.accenturechallenge.core.PokemonUiModel
import com.example.accenturechallenge.core.generation
import com.example.accenturechallenge.data.database.entities.DbPokemonWithOrWithoutFavorites
import com.example.accenturechallenge.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    /**
     * Fetches pokemon with or without favorites
     * Inserts separators on each generation change as defined on [PokemonUiModel]
     */
    fun fetchPokemons(): Flow<PagingData<PokemonUiModel>>{
        val newResult: Flow<PagingData<PokemonUiModel>> = repository.fetchPokemons()
            .map { pagingData -> pagingData.map { PokemonUiModel.PokemonItem(it) } }
            .map{
                it.insertSeparators<PokemonUiModel.PokemonItem, PokemonUiModel> { before, after ->
                    if (after == null) {
                        //end of list
                        return@insertSeparators null
                    }

                    if(before == null){
                        //beginning of the list
                        return@insertSeparators PokemonUiModel.SeparatorItem("Generation ${after.generation}")
                    }

                    //between two items
                    if(before.generation < after.generation) {
                        PokemonUiModel.SeparatorItem("Generation ${after.generation}")
                    } else {
                        //no separator
                        null
                    }
                }
            }
            .cachedIn(viewModelScope)

        return newResult
    }

    fun favoritePokemon(pokemonWithOrWithoutWithFavorite: DbPokemonWithOrWithoutFavorites){
        viewModelScope.launch {
            repository.favoritePokemon(pokemonWithOrWithoutWithFavorite)
        }
    }

}
