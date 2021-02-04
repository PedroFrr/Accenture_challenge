package com.example.accenturechallenge.data.network.pokemonapi.mapper

import com.example.accenturechallenge.data.database.entities.DbPokemon
import com.example.accenturechallenge.data.network.pokemonapi.response.PokemonResult


interface ApiMapper {

    fun mapApiPokemonToModel(apiPokemon: PokemonResult): DbPokemon

}