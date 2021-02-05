package com.example.accenturechallenge.ui.pokemons.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.accenturechallenge.data.Result
import com.example.accenturechallenge.data.database.entities.DbPokemon
import com.example.accenturechallenge.data.network.pokemonapi.response.GetPokemonItemResult
import com.example.accenturechallenge.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _pokemonDetail = MutableLiveData<Result<GetPokemonItemResult>>()
    fun getPokemonDetail(): LiveData<Result<GetPokemonItemResult>> = _pokemonDetail

    private val _dbPokemon = MutableLiveData<DbPokemon>()
    fun getDbPokemon(): LiveData<DbPokemon> = _dbPokemon

    fun fetchPokemonDetail(pokemonId: String){
        viewModelScope.launch {
            repository.fetchPokemonDetail(pokemonId)
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
                    _dbPokemon.value = it
                }

        }
    }

    fun favoritePokemon(){
        viewModelScope.launch {
            _dbPokemon.value?.let { repository.favoritePokemon(it) }
        }
    }

}