package com.example.accenturechallenge.ui.pokemons.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.accenturechallenge.data.database.entities.DbPokemonWithOrWithoutFavorites
import com.example.accenturechallenge.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _favoritePokemons = MutableLiveData<List<DbPokemonWithOrWithoutFavorites>>()
    fun getFavoritePokemons(): LiveData<List<DbPokemonWithOrWithoutFavorites>> = _favoritePokemons

    init {
        viewModelScope.launch {
            repository.fetchFavoritePokemons()
                .collect {
                    //orders list in alphabetical order
                    val sortedList = it.sortedBy { pokemonResult ->   pokemonResult.pokemon.name }
                    _favoritePokemons.postValue(sortedList)
                }

        }
    }

    fun favoritePokemon(pokemonWithFavorites: DbPokemonWithOrWithoutFavorites) {
        viewModelScope.launch {
            repository.favoritePokemon(pokemonWithFavorites)
        }
    }
}
