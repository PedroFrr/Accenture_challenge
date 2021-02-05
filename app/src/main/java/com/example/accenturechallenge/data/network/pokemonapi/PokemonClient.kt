package com.example.accenturechallenge.data.network.pokemonapi

import com.example.accenturechallenge.data.Failure
import com.example.accenturechallenge.data.Success
import javax.inject.Inject

class PokemonClient @Inject constructor(
    private val pokemonService: PokemonService
) {

    suspend fun fetchPokemons(offset: Int, itemsPerPage: Int) = pokemonService.fetchPokemons(offset, itemsPerPage)

    suspend fun fetchPokemonDetail(pokemonId: String) = try {
        val pokemonDetail = pokemonService.fetchPokemonDetail(pokemonId)

        Success(pokemonDetail)
    }catch (exception: Exception){
        Failure(exception)
    }
}