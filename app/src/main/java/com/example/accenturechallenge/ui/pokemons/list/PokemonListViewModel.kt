package com.example.accenturechallenge.ui.pokemons.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.accenturechallenge.data.database.entities.DbPokemon
import com.example.accenturechallenge.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    fun fetchPokemons(): Flow<PagingData<DbPokemon>>{
        return repository.fetchPokemons().cachedIn(viewModelScope)
    }

    fun favoritePokemon(pokemon: DbPokemon){
        viewModelScope.launch {
            repository.favoritePokemon(pokemon)
        }
    }

}
