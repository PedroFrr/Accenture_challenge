package com.example.accenturechallenge.ui.pokemons.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.accenturechallenge.data.Result
import com.example.accenturechallenge.data.database.entities.DbPokemonWithAbilitiesAndTypes
import com.example.accenturechallenge.data.database.entities.DbPokemonWithOrWithoutFavorites
import com.example.accenturechallenge.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    //TODO working solution
//    private val _pokemonDetail = MutableLiveData<Result<DbPokemonDetailWithAbilities>>()
//    fun getPokemonDetail(): LiveData<Result<DbPokemonDetailWithAbilities>> = _pokemonDetail

    private val _pokemonDetail = MutableLiveData<Result<DbPokemonWithAbilitiesAndTypes>>()
    fun getPokemonDetail(): LiveData<Result<DbPokemonWithAbilitiesAndTypes>> = _pokemonDetail

    private val _pokemonWithOrWithoutFavorites = MutableLiveData<DbPokemonWithOrWithoutFavorites>()
    fun getDbPokemon(): LiveData<DbPokemonWithOrWithoutFavorites> = _pokemonWithOrWithoutFavorites

    //TODO working solution
//    fun fetchPokemonDetail(pokemonId: String){
//        viewModelScope.launch {
//            repository.fetchPokemonDetail(pokemonId)
//                .collect {
//                    _pokemonDetail.postValue(it)
//                }
//        }
//    }

        fun fetchPokemonDetail(pokemonId: String){
        viewModelScope.launch {
            repository.fetchPokemonDetailWithAbilitiesAndTypes(pokemonId)
                .collect {
                    _pokemonDetail.postValue(it)
                }
        }
    }

    //Flow allow us to get immediate response from DB (in this case if favorite changes)
    fun getDbPokemon(pokemonId: String){
        viewModelScope.launch {
            repository.getDbPokemonDetail(pokemonId)
                .collect{
                    _pokemonWithOrWithoutFavorites.value = it
                }

        }
    }

    fun favoritePokemon(){
        viewModelScope.launch {
            _pokemonWithOrWithoutFavorites.value?.let { repository.favoritePokemon(it) }
        }
    }

}