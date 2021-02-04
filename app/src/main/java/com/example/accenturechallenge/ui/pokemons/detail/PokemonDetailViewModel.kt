package com.example.accenturechallenge.ui.pokemons.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.accenturechallenge.data.Result
import com.example.accenturechallenge.data.network.pokemonapi.response.GetPokemonItemResult
import com.example.accenturechallenge.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _pokemonDetail = MutableLiveData<Result<GetPokemonItemResult>>()
    fun getPokemonDetail(): LiveData<Result<GetPokemonItemResult>> = _pokemonDetail

    fun fetchPokemonDetail(pokemonId: String){
        viewModelScope.launch {
            val pokemon = repository.fetchPokemonDetail(pokemonId)
            _pokemonDetail.postValue(pokemon)
        }
    }

}